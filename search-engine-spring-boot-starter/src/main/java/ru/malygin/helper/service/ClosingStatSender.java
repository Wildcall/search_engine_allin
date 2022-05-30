package ru.malygin.helper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@RequiredArgsConstructor
public class ClosingStatSender implements ApplicationListener<ContextClosedEvent> {

    private final LogSender logSender;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        logSender.info("Application close");
    }
}
