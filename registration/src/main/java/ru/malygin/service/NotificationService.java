package ru.malygin.service;

import ru.malygin.helper.model.Notification;
import ru.malygin.model.entity.AppUser;

public interface NotificationService {
    void send(Notification notification);

    void sendConfirmNotification(AppUser appUser);

    void sendSuccessNotification(AppUser appUser);
}
