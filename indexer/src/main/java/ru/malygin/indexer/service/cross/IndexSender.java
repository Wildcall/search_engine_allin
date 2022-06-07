package ru.malygin.indexer.service.cross;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Schedulers;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.DefaultQueueDeclareService;
import ru.malygin.helper.service.cross.DataSender;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.indexer.service.IndexService;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndexSender implements DataSender {

    private final RabbitTemplate rabbitTemplate;
    private final LogSender logSender;
    private final DefaultQueueDeclareService declareService;
    private final IndexService indexService;

    @Override
    public void send(Long itemCount,
                     DataRequest dataRequest) {
        declareService.createQueue(dataRequest.getDataQueue(), null);
        indexService
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
