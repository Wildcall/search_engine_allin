package ru.malygin.notification.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.malygin.logsenderspringbootstarter.service.LogSender;
import ru.malygin.notification.facade.NotificationFacade;
import ru.malygin.notification.model.Notification;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationReceiver {

    private final LogSender logSender;
    private final NotificationFacade facade;

    @RabbitListener(
            queuesToDeclare = @Queue(
                    name = "${rabbit.queues.notification-queue}",
                    durable = "false",
                    autoDelete = "false"),
            messageConverter = "jsonConverter")
    public void receive(Notification notification) {
        try {
            logSender.info("RECEIVE / Type: %s", notification.getType());
            facade.send(notification);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
    }
}
