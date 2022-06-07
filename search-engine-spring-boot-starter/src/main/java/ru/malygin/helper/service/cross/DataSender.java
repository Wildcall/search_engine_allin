package ru.malygin.helper.service.cross;

import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.senders.LogSender;

public interface DataSender {
    void send(Long itemCount,
              DataRequest dataRequest);

    default void sendLog(LogSender logSender,
                         DataRequest dataRequest,
                         Long itemCount) {
        logSender.info("SEND DATA / Count: %s / Task id: %s / Queue: %s ",
                       itemCount,
                       dataRequest.getTaskId(),
                       dataRequest.getDataQueue());
    }
}
