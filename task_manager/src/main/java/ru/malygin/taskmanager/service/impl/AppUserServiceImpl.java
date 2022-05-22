package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.repository.AppUserRepository;
import ru.malygin.taskmanager.service.AppUserService;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUser save(String email) {
        AppUser appUser = new AppUser(email);
        if (appUser.hasRequiredField()) {
            log.info("APP_USER SAVE / Email: {}", appUser.getEmail());
            return appUserRepository.save(new AppUser(email));
        }
        throw new IllegalArgumentException("Failed to save appUser");
    }

    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository
                .findByEmail(email)
                .orElseGet(() -> save(email));
    }

    @Override
    public AppUser findByAuthentication(Authentication authentication) {
        String email = authentication.getName();
        if (email == null) throw new BadRequestException("Invalid email");
        return findByEmail(authentication.getName());
    }

    @Override
    public void updateLastActionTime(AppUser appUser) {
        appUser.setLastActionTime(LocalDateTime.now());
        appUserRepository.save(appUser);
    }
}
