package ru.malygin.event;

import org.springframework.context.ApplicationEvent;
import ru.malygin.model.Notification;

public class NotificationEvent extends ApplicationEvent {

    public NotificationEvent(Notification notification) {
        super(notification);
    }
}
