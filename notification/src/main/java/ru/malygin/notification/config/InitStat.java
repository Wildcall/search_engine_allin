package ru.malygin.crawler.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.malygin.crawler.rabbit.LogSender;

import javax.annotation.PreDestroy;

@RequiredArgsConstructor
@Component
public class InitStat {

    private final LogSender logSender;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        logSender.send("Application start");
    }

    @PreDestroy
    public void onDestroy() {
        logSender.send("Application close");
    }
}
