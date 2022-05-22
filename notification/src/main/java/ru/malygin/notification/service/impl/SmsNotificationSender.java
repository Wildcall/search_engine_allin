package ru.malygin.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.notification.model.entity.impl.Notification;
import ru.malygin.notification.service.NotificationSender;

import static ru.malygin.notification.service.NotificationSenderType.SMS;

@Slf4j
@RequiredArgsConstructor
@Service
public class SmsNotificationSender implements NotificationSender {

    @Override
    public String send(Notification notification) {
        log.info("SmsNotificationSender");
        return "SmsNotificationSender";
    }

    @Override
    public String getType() {
        return SMS;
    }
}
