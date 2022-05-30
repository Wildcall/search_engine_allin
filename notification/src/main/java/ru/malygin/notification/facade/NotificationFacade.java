package ru.malygin.notification.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.logsenderspringbootstarter.service.LogSender;
import ru.malygin.notification.model.Notification;
import ru.malygin.notification.service.NotificationSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationFacade {

    private final LogSender logSender;
    private final Map<String, NotificationSender> map;

    public NotificationFacade(LogSender logSender,
                              List<NotificationSender> notificationSenders) {
        this.logSender = logSender;
        map = notificationSenders
                .stream()
                .collect(Collectors.toMap(NotificationSender::getType, Function.identity()));
    }

    public void send(Notification notification) {
        String type = notification.getType();
        NotificationSender notificationSender = map.get(type);
        if (notificationSender != null) {
            notificationSender.send(notification);
            return;
        }
        logSender.error("Type %s not supported", type);
    }
}
