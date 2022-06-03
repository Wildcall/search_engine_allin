package ru.malygin.notification.service;

import ru.malygin.helper.model.Notification;

public interface NotificationService {
    void send(Notification notification);

    String getType();
}
