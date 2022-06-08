package ru.malygin.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.Notification;
import ru.malygin.helper.senders.LogSender;
import ru.malygin.notification.service.NotificationService;

import static ru.malygin.notification.service.NotificationServiceType.TELEGRAM;

@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramNotificationService implements NotificationService {

    private final LogSender logSender;

    @Override
    public void send(Notification notification) {
        logSender.info("TelegramNotificationSender not impl");
    }

    @Override
    public String getType() {
        return TELEGRAM;
    }
}
