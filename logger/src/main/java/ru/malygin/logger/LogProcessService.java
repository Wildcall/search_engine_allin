package ru.malygin.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class LogProcessService {

    public void processErrorLog(Map<String, String> errorMap) {
        log.error(errorMap.toString());
    }

    public void processInfoLog(Map<String, String> infoMap) {
        log.info(infoMap.toString());
    }
}
