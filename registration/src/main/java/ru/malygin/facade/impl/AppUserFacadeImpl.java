package ru.malygin.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.event.NotificationEvent;
import ru.malygin.facade.AppUserFacade;
import ru.malygin.model.AuthResponse;
import ru.malygin.model.Notification;
import ru.malygin.model.dto.AppUserDto;
import ru.malygin.model.entity.AppUser;
import ru.malygin.model.entity.Role;
import ru.malygin.security.JwtUtil;
import ru.malygin.service.AppUserService;
import ru.malygin.service.NotificationService;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserFacadeImpl implements AppUserFacade {

    private final AppUserService appUserService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse save(AppUserDto appUserDto) {
        AppUser appUser = appUserDto.toAppUser();
        appUser = appUserService.save(appUser);
        Notification notification = notificationService.createConfirmNotification(appUser);
        applicationEventPublisher.publishEvent(new NotificationEvent(notification));
        return new AuthResponse(jwtUtil.generateAccessToken(appUser),
                                jwtUtil.generateRefreshToken(appUser),
                                appUser.getId(),
                                appUser.getEmail(),
                                appUser
                                                             .getRoles()
                                                             .stream()
                                                             .map(Role::getName)
                                                             .toList());
    }

    @Override
    public String resendConfirmEmail(Authentication authentication) {
        String email = authentication.getName();
        AppUser appUser = appUserService.availableResendConfirmEmail(email);
        Notification notification = notificationService.createConfirmNotification(appUser);
        applicationEventPublisher.publishEvent(new NotificationEvent(notification));
        return "OK";
    }

    @Override
    public Map<String, String> refreshAccessToken(Authentication authentication) {
        String email = authentication.getName();
        AppUser appUser = appUserService.findByEmail(email);
        return Map.of("access_token", jwtUtil.generateAccessToken(appUser));
    }

    @Override
    public String confirmEmail(Authentication authentication) {
        String email = authentication.getName();
        AppUser appUser = appUserService.confirmEmail(email);
        Notification notification = notificationService.createSuccessNotification(appUser);
        applicationEventPublisher.publishEvent(new NotificationEvent(notification));
        return "OK";
    }

    @Override
    public AppUserDto getUserData(Authentication authentication) {
        String email = authentication.getName();
        AppUser appUser = appUserService.findByEmail(email);
        return appUser.toAppUserDto();
    }
}
