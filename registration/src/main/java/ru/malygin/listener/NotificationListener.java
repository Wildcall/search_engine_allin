package ru.malygin.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import ru.malygin.event.NotificationEvent;
import ru.malygin.model.Notification;
import ru.malygin.service.NotificationService;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationListener implements ApplicationListener<NotificationEvent> {

    private final NotificationService notificationService;

    @Override
    public void onApplicationEvent(NotificationEvent event) {
        Notification notification = (Notification) event.getSource();
        log.info("NotificationEvent / {}", notification.getSendTo());
        new Thread(() -> notificationService.send(notification)).start();
    }
}
