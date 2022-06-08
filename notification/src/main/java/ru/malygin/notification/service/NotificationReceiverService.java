package ru.malygin.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.Notification;
import ru.malygin.helper.senders.LogSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationReceiverService {

    static {
        log.info("[o] Create NotificationReceiverService in application");
    }

    private final Map<String, NotificationService> map;
    private final LogSender logSender;

    public NotificationReceiverService(LogSender logSender,
                                       List<NotificationService> notificationServices) {
        this.logSender = logSender;
        map = notificationServices
                .stream()
                .collect(Collectors.toMap(NotificationService::getType, Function.identity()));
    }

    @RabbitListener(queues = "#{properties.getCommon().getNotification().getNotificationRoute()}")
    public void receiveInfoLog(Notification n,
                               @Header("app") String app) {
        String type = n.getType();
        NotificationService notificationService = map.get(type);
        if (notificationService == null) {
            logSender.error("NOTIFICATION TYPE NOT SUPPORTED / Type: %s / From: %s / Template: %s / Send to: %s",
                            n.getType(), app,
                            n.getTemplate(), n.getSendTo());
            return;
        }

        logSender.info("NOTIFICATION RECEIVE / Type: %s / From: %s / Template: %s / Send to: %s", n.getType(), app,
                       n.getTemplate(),
                       n.getSendTo());
        notificationService.send(n);
    }
}
