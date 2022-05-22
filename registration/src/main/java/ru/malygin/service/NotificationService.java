package ru.malygin.service;

import ru.malygin.model.Notification;
import ru.malygin.model.entity.AppUser;

public interface NotificationService {
    void send(Notification notification);

    Notification createConfirmNotification(AppUser appUser);

    Notification createSuccessNotification(AppUser appUser);
}
