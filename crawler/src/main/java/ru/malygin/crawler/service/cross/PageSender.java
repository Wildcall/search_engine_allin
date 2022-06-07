package ru.malygin.crawler.service.cross;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;
import ru.malygin.crawler.service.PageService;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.DefaultQueueDeclareService;
import ru.malygin.helper.service.cross.DataSender;
import ru.malygin.helper.service.senders.LogSender;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageSender implements DataSender {

    private final RabbitTemplate rabbitTemplate;
    private final PageService pageService;
    private final LogSender logSender;
    private final DefaultQueueDeclareService declareService;

    @Override
    public void send(Long itemCount,
                     DataRequest dataRequest) {
        declareService.createQueue(dataRequest.getDataQueue(), null);
        pageService
                .findAllBySiteIdAndAppUserId(dataRequest.getSiteId(), dataRequest.getAppUserId())
                .subscribeOn(Schedulers.boundedElastic())
                .doOnNext(page -> rabbitTemplate.convertAndSend(dataRequest.getDataQueue(), page))
                .doOnComplete(() -> {
                    DataSender.super.sendLog(logSender, dataRequest, itemCount);
                    declareService.removeQueue(dataRequest.getDataQueue());
                })
                .subscribe();
    }
}
