package ru.malygin.crawler.service.cross;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.malygin.crawler.service.PageService;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.cross.DataRequestListener;
import ru.malygin.helper.service.cross.DataSender;
import ru.malygin.helper.service.senders.LogSender;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageRequestListener implements DataRequestListener {

    private final PageService pageService;
    private final DataSender pageSender;
    private final LogSender logSender;

    @Override
    @RabbitListener(queues = "#{properties.getCommon().getRequest().getPageRoute()}")
    public Long dataRequestListen(DataRequest dataRequest) {
        DataRequestListener.super.sendLog(logSender, dataRequest);
        return pageService
                .getCountBySiteIdAndAppUserId(dataRequest.getSiteId(), dataRequest.getAppUserId())
                .doOnSuccess(pageCount -> DataRequestListener.super.sendData(pageSender, pageCount, dataRequest))
                .block();
    }
}
