package ru.malygin.taskmanager.service;

import org.springframework.security.core.Authentication;
import ru.malygin.taskmanager.model.entity.impl.AppUser;

public interface AppUserService {

    AppUser save(String email);

    AppUser findByEmail(String email);

    AppUser findByAuthentication(Authentication authentication);

    void updateLastActionTime(AppUser appUser);
}
