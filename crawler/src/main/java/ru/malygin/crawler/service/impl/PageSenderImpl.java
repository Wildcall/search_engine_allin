package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;
import ru.malygin.crawler.service.PageSender;
import ru.malygin.crawler.service.PageService;
import ru.malygin.helper.model.PageRequest;
import ru.malygin.helper.service.DefaultQueueDeclareService;
import ru.malygin.helper.service.senders.LogSender;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageSenderImpl implements PageSender {

    private final RabbitTemplate rabbitTemplate;
    private final PageService pageService;
    private final LogSender logSender;
    private final DefaultQueueDeclareService declareService;

    @Override
    public void send(Long pageCount,
                     PageRequest pageRequest) {
        declareService.createQueue(pageRequest.getPageQueue(), null);
        pageService
                .findAllBySiteIdAndAppUserId(pageRequest.getSiteId(), pageRequest.getAppUserId())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(
                        page -> rabbitTemplate.convertAndSend(pageRequest.getPageQueue(), page))
                .doOnComplete(
                        () -> {
                            logSender.info("CRAWLER SEND PAGE / Count: %s / Queue: %s / Task id: %s", pageCount,
                                           pageRequest.getPageQueue(), pageRequest.getTaskId());
                            declareService.removeQueue(pageRequest.getPageQueue());
                        })

                .subscribe();
    }
}
