package ru.malygin.taskmanager.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malygin.taskmanager.model.ResourceCallback;
import ru.malygin.taskmanager.service.CallbackService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/callback")
public class CallbackController {

    private final CallbackService callbackService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> callBackListener(@RequestBody ResourceCallback resourceCallback) {
        callbackService.process(resourceCallback);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("OK");
    }
}
