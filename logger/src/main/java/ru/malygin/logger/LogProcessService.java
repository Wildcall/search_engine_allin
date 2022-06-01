package ru.malygin.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.LogReceiveEvent;

import java.util.Map;

@Slf4j
@Service
public class LogProcessService {

    @EventListener(LogReceiveEvent.class)
    public void processLog(LogReceiveEvent event) {
        String type = event.getType();
        Map<String, String> eventMap = event.getMap();
        if ("error".equals(type)) {
            log.error(eventMap.toString());
            return;
        }
        if ("info".equals(type)) {
            log.info(eventMap.toString());
            return;
        }
        log.debug(eventMap.toString());
    }
}
