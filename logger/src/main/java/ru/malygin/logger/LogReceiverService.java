package ru.malygin.logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogReceiverService {

    static {
        log.info("[o] Create LogReceiver in application");
    }

    private final LogProcessService logProcessService;

    @RabbitListener(queues = "#{properties.getCommon().getLog().getInfoRoute()}")
    public void receiveInfoLog(Map<String, String> map,
                               @Header("app") String app,
                               Message message) {
        map.put("Application", app);
        map.put("Time", String.valueOf(message
                                               .getMessageProperties()
                                               .getTimestamp()
                                               .getTime()));
        logProcessService.processInfoLog(map);
    }

    @RabbitListener(queues = "#{properties.getCommon().getLog().getErrorRoute()}")
    public void receiveErrorLog(Map<String, String> map,
                                @Header("app") String app,
                                Message message) {
        map.put("Application", app);
        map.put("Time", String.valueOf(message
                                               .getMessageProperties()
                                               .getTimestamp()
                                               .getTime()));
        logProcessService.processErrorLog(map);
    }
}
