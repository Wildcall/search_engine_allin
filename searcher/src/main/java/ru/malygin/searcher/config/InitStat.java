package ru.malygin.searcher.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.malygin.logsenderspringbootstarter.service.LogSender;

import javax.annotation.PreDestroy;

@RequiredArgsConstructor
@Component
public class InitStat {

    private final LogSender logSender;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        logSender.info("Application start");
    }

    @PreDestroy
    public void onDestroy() {
        logSender.info("Application close");
    }
}
