package ru.malygin.notification.service;

import ru.malygin.notification.model.Notification;

public interface NotificationSender {
    void send(Notification notification);

    String getType();
}
