package ru.malygin.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.LogReceiveEvent;

@Slf4j
@Service
public class LogProcessService {

    @EventListener(LogReceiveEvent.class)
    public void processLog(LogReceiveEvent event) {
        String type = event.getType();
        String message = event.getMessage();
        if ("error".equals(type)) {
            log.error(message);
            return;
        }
        if ("info".equals(type)) {
            log.info(message);
            return;
        }
        log.debug(message);
    }
}
