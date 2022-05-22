package ru.malygin.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.notification.model.entity.impl.Notification;
import ru.malygin.notification.service.NotificationSender;

import static ru.malygin.notification.service.NotificationSenderType.TELEGRAM;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramNotificationSender implements NotificationSender {

    @Override
    public String send(Notification notification) {
        log.info("TelegramNotificationSender");
        return "TelegramNotificationSender";
    }

    @Override
    public String getType() {
        return TELEGRAM;
    }
}
