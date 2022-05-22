package ru.malygin.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.malygin.model.entity.AppUser;

public interface AppUserService extends UserDetailsService {

    AppUser save(AppUser appUser);

    AppUser confirmEmail(String email);

    AppUser findByEmail(String email);

    AppUser availableResendConfirmEmail(String email);
}
