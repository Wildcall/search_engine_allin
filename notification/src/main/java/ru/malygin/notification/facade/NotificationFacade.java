package ru.malygin.notification.facade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.notification.exception.BadRequestException;
import ru.malygin.notification.model.dto.impl.NotificationDto;
import ru.malygin.notification.model.entity.impl.Notification;
import ru.malygin.notification.service.NotificationSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class NotificationFacade {

    private final Map<String, NotificationSender> map;

    public NotificationFacade(List<NotificationSender> notificationSenders) {
        map = notificationSenders
                .stream()
                .collect(Collectors.toMap(NotificationSender::getType, Function.identity()));
    }

    public String send(NotificationDto notificationDto) {
        String type = notificationDto.getType();
        NotificationSender notificationSender = map.get(type);
        if (notificationSender == null)
            throw new BadRequestException(type + " not supported");
        return notificationSender.send((Notification) notificationDto.toBaseEntity());
    }
}
