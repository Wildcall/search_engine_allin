package ru.malygin.helper.service.cross;

import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.senders.LogSender;

public interface DataRequestListener {

    Long dataRequestListen(DataRequest dataRequest);

    default void sendLog(LogSender logSender,
                         DataRequest dataRequest) {
        logSender.info("DATA REQUEST RECEIVE / Task id: %s / Site id: %s / AppUser id: %s / Response queue: %s",
                       dataRequest.getTaskId(), dataRequest.getSiteId(), dataRequest.getAppUserId(),
                       dataRequest.getDataQueue());
    }

    default void sendData(DataSender dataSender,
                          Long itemCount,
                          DataRequest dataRequest) {
        dataSender.send(itemCount, dataRequest);
    }
}
