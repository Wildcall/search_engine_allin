package ru.malygin.registration.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.registration.model.AuthResponse;
import ru.malygin.registration.model.dto.AppUserDto;
import ru.malygin.registration.model.entity.AppUser;
import ru.malygin.registration.model.entity.Role;
import ru.malygin.registration.security.JwtUtil;
import ru.malygin.registration.service.AppUserService;
import ru.malygin.registration.service.NotificationService;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class AppUserFacadeImpl implements AppUserFacade {

    private final AppUserService appUserService;
    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse save(AppUserDto appUserDto) {
        AppUser appUser = appUserDto.toAppUser();
        appUser = appUserService.save(appUser);
        notificationService.sendConfirmNotification(appUser);
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
        notificationService.sendConfirmNotification(appUser);
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
        notificationService.sendSuccessNotification(appUser);
        return "OK";
    }

    @Override
    public AppUserDto getUserData(Authentication authentication) {
        String email = authentication.getName();
        AppUser appUser = appUserService.findByEmail(email);
        return appUser.toAppUserDto();
    }
}
