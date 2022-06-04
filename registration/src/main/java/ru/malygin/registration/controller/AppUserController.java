package ru.malygin.registration.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.malygin.registration.facade.AppUserFacade;
import ru.malygin.registration.model.AuthResponse;
import ru.malygin.registration.model.dto.AppUserDto;
import ru.malygin.registration.model.dto.view.AppUserView;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class AppUserController {

    private final AppUserFacade appUserFacade;

    @PostMapping(path = "/registration", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> save(@RequestBody @Validated(AppUserView.New.class) AppUserDto appUserDto) {
        AuthResponse response = appUserFacade.save(appUserDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> refresh(Authentication authentication) {
        Map<String, String> response = appUserFacade.refreshAccessToken(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(AppUserView.Response.class)
    public ResponseEntity<String> confirm(Authentication authentication) {
        String response = appUserFacade.confirmEmail(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/resend", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(AppUserView.Response.class)
    public ResponseEntity<String> resend(Authentication authentication) {
        String response = appUserFacade.resendConfirmEmail(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(AppUserView.Response.class)
    public ResponseEntity<AppUserDto> getUserInfo(Authentication authentication) {
        AppUserDto response = appUserFacade.getUserData(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(AppUserView.Response.class)
    public ResponseEntity<String> getStatus() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("OK");
    }
}
