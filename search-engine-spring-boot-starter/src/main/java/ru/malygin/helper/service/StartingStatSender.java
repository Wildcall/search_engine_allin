package ru.malygin.helper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@RequiredArgsConstructor
public class StartingStatSender implements ApplicationListener<ContextRefreshedEvent> {

    private final LogSender logSender;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logSender.info("Application start");
    }
}
