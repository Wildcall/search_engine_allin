package ru.malygin.notification.service;

import ru.malygin.notification.model.entity.impl.Notification;

public interface NotificationSender {
    String send(Notification notification);

    String getType();
}
