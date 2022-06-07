package ru.malygin.indexer.service.cross;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.cross.DataRequestListener;
import ru.malygin.helper.service.cross.DataSender;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.indexer.service.LemmaService;

@Slf4j
@RequiredArgsConstructor
@Service
public class LemmaRequestListener implements DataRequestListener {

    private final LemmaService lemmaService;
    private final DataSender lemmaSender;
    private final LogSender logSender;

    @Override
    @RabbitListener(queues = "#{properties.getCommon().getRequest().getLemmaRoute()}")
    public Long dataRequestListen(DataRequest dataRequest) {
        DataRequestListener.super.sendLog(logSender, dataRequest);
        return lemmaService
                .getCountBySiteIdAndAppUserId(dataRequest.getSiteId(), dataRequest.getAppUserId())
                .doOnSuccess(lemmaCount -> DataRequestListener.super.sendData(lemmaSender, lemmaCount, dataRequest))
                .block();
    }
}
