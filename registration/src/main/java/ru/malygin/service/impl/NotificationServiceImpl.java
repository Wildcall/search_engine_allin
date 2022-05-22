package ru.malygin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import ru.malygin.config.ResourceConfig;
import ru.malygin.config.WebClientConfiguration;
import ru.malygin.model.Notification;
import ru.malygin.model.ResourceType;
import ru.malygin.model.entity.AppUser;
import ru.malygin.security.JwtUtil;
import ru.malygin.service.NotificationService;

import java.time.format.DateTimeFormatter;
import java.util.Map;

// TODO: 03.05.2022 Возможно добавить кеш отправляемых сообщений

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final JwtUtil jwtUtil;
    private final ResourceConfig resourceConfig;
    private final WebClientConfiguration webClientConfiguration;
    @Value("${email.callback}")
    private String callbackAddress;

    @Override
    public void send(Notification notification) {
        log.info("Send notification / {}", notification.getSendTo());
        try {
            webClientConfiguration
                    .getWebClient()
                    .post()
                    .uri(resourceConfig
                                 .getResource(ResourceType.NOTIFICATION)
                                 .getBaseUrl())
                    .body(BodyInserters.fromValue(notification))
                    .header(HttpHeaders.AUTHORIZATION,
                            "Bearer " + jwtUtil.generateResourceToken(ResourceType.NOTIFICATION))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
        }
    }

    @Override
    public Notification createConfirmNotification(AppUser appUser) {
        String type = "email";
        String sendTo = appUser.getEmail();
        String subject = "Confirm email";
        String template = "confirm";
        String name = sendTo.substring(0, sendTo.indexOf("@"));
        String registrationDate = appUser
                .getCreateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        String confirmLink = callbackAddress + jwtUtil.generateConfirmToken(appUser);
        Map<String, String> payload = Map.of("name", name, "email", sendTo, "registrationDate", registrationDate,
                                             "confirmLink", confirmLink);

        return new Notification(type, sendTo, subject, template, payload);
    }

    @Override
    public Notification createSuccessNotification(AppUser appUser) {
        String type = "email";
        String sendTo = appUser.getEmail();
        String subject = "Success registration";
        String template = "success";
        String name = sendTo.substring(0, sendTo.indexOf("@"));
        String registrationDate = appUser
                .getCreateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Map<String, String> payload = Map.of("name", name, "email", sendTo, "registrationDate", registrationDate);

        return new Notification(type, sendTo, subject, template, payload);
    }
}
