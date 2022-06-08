package ru.malygin.registration.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.registration.config.RegistrationServiceProperties;
import ru.malygin.helper.model.Notification;
import ru.malygin.helper.senders.LogSender;
import ru.malygin.helper.senders.NotificationSender;
import ru.malygin.registration.model.entity.AppUser;
import ru.malygin.registration.security.JwtUtil;
import ru.malygin.registration.service.NotificationService;

import java.time.format.DateTimeFormatter;
import java.util.Map;

// TODO: 03.05.2022 Возможно добавить кеш отправляемых сообщений

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final JwtUtil jwtUtil;
    private final NotificationSender notificationSender;
    private final LogSender logSender;
    private final RegistrationServiceProperties regProperties;

    @Override
    public void send(Notification n) {
        notificationSender.send(n);

        logSender.info("NOTIFICATION SEND / Type: %s / Template: %s / Send to: %s", n.getType(), n.getTemplate(),
                       n.getSendTo());
    }

    @Override
    public void sendConfirmNotification(AppUser appUser) {
        String type = "email";
        String sendTo = appUser.getEmail();
        String subject = "Confirm email";
        String template = "confirm";
        String name = sendTo.substring(0, sendTo.indexOf("@"));
        String registrationDate = appUser
                .getCreateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String confirmLink = regProperties
                .getEmail()
                .getCallbackAddress() + jwtUtil.generateConfirmToken(appUser);
        Map<String, String> payload = Map.of("name", name, "email", sendTo, "registrationDate", registrationDate,
                                             "confirmLink", confirmLink);

        send(new Notification(type, sendTo, subject, template, payload));
    }

    @Override
    public void sendSuccessNotification(AppUser appUser) {
        String type = "email";
        String sendTo = appUser.getEmail();
        String subject = "Success registration";
        String template = "success";
        String name = sendTo.substring(0, sendTo.indexOf("@"));
        String registrationDate = appUser
                .getCreateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Map<String, String> payload = Map.of("name", name, "email", sendTo, "registrationDate", registrationDate);

        send(new Notification(type, sendTo, subject, template, payload));
    }
}
