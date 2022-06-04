package ru.malygin.registration.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.malygin.registration.model.entity.AppUser;

public interface AppUserService extends UserDetailsService {

    AppUser save(AppUser appUser);

    AppUser confirmEmail(String email);

    AppUser findByEmail(String email);

    AppUser availableResendConfirmEmail(String email);
}
