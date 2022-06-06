package ru.malygin.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@Service
public class LogProcessService {

    public void processErrorLog(Map<String, String> errorMap) {
        try {
            String app = errorMap.get("Application");
            String timeString = errorMap.get("Time");
            String message = errorMap.get("message");
            if (app != null && timeString != null) {
                long time = Long.parseLong(timeString);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formatTime = LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(time), TimeZone
                                .getDefault()
                                .toZoneId())
                        .format(formatter);
                log.error("{} / {} / {}", app, formatTime, message);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void processInfoLog(Map<String, String> infoMap) {
        try {
            String app = infoMap.get("Application");
            String timeString = infoMap.get("Time");
            String message = infoMap.get("message");
            if (app != null && timeString != null) {
                long time = Long.parseLong(timeString);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formatTime = LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(time), TimeZone
                                .getDefault()
                                .toZoneId())
                        .format(formatter);
                log.info("{} / {} / {}", app, formatTime, message);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
