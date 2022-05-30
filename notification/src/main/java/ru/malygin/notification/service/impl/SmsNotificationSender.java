package ru.malygin.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.logsenderspringbootstarter.service.LogSender;
import ru.malygin.notification.model.Notification;
import ru.malygin.notification.service.NotificationSender;

import static ru.malygin.notification.service.NotificationSenderType.SMS;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsNotificationSender implements NotificationSender {

    private final LogSender logSender;

    @Override
    public void send(Notification notification) {
        logSender.info("SmsNotificationSender not impl");
    }

    @Override
    public String getType() {
        return SMS;
    }
}
