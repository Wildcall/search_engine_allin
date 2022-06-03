package ru.malygin.helper.service.senders;

import ru.malygin.helper.model.Notification;

public interface NotificationSender {
    void send(Notification n);
}
