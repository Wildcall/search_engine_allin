package ru.malygin.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malygin.notification.facade.NotificationFacade;
import ru.malygin.notification.model.dto.impl.NotificationDto;
import ru.malygin.notification.model.dto.view.View;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationFacade notificationFacade;

    @PostMapping(path = "send", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(@RequestBody @Validated(View.Request.class) NotificationDto notificationDto) {
        String response = notificationFacade.send(notificationDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
