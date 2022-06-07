package ru.malygin.indexer.service.cross;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.cross.DataRequestListener;
import ru.malygin.helper.service.cross.DataSender;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.indexer.service.IndexService;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndexRequestListener implements DataRequestListener {

    private final IndexService indexService;
    private final DataSender indexSender;
    private final LogSender logSender;

    @Override
    @RabbitListener(queues = "#{properties.getCommon().getRequest().getIndexRoute()}")
    public Long dataRequestListen(DataRequest dataRequest) {
        DataRequestListener.super.sendLog(logSender, dataRequest);
        return indexService
                .getCountBySiteIdAndAppUserId(dataRequest.getSiteId(), dataRequest.getAppUserId())
                .doOnSuccess(indexCount -> DataRequestListener.super.sendData(indexSender, indexCount, dataRequest))
                .block();
    }
}
