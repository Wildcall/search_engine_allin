package ru.malygin.registration.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malygin.registration.config.RegistrationServiceProperties;
import ru.malygin.registration.exception.BadRequestException;
import ru.malygin.registration.model.Roles;
import ru.malygin.registration.model.entity.AppUser;
import ru.malygin.registration.model.entity.Role;
import ru.malygin.registration.repository.AppUserRepository;
import ru.malygin.registration.service.RoleService;
import ru.malygin.registration.service.AppUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationServiceProperties regProperties;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return findByEmail(email);
        } catch (BadRequestException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    @Override
    public AppUser save(AppUser appUser) {
        if (appUserRepository.existsByEmail(appUser.getEmail())) throw new BadRequestException(
                "An account for that email already exists");
        try {
            appUser.setCreateTime(LocalDateTime.now());
            appUser.setLastActionTime(appUser.getCreateTime());
            appUser.setEnable(true);
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            Role roleNew = roleService.findByName(Roles.NEW);
            appUser.setRoles(List.of(roleNew));
            appUserRepository.save(appUser);
            return appUser;
        } catch (Exception e) {
            throw new BadRequestException("Unable to register, please try again later");
        }
    }

    @Override
    public AppUser confirmEmail(String email) {
        AppUser appUser = findByEmail(email);
        Role roleNew = roleService.findByName(Roles.NEW);
        if (!appUser
                .getRoles()
                .contains(roleNew)) {
            throw new BadRequestException("Email already verified");
        }
        appUser
                .getRoles()
                .remove(roleNew);
        Role roleUser = roleService.findByName(Roles.USER);
        appUser
                .getRoles()
                .add(roleUser);
        appUser.setLastActionTime(LocalDateTime.now());
        return appUserRepository.save(appUser);
    }

    @Override
    public AppUser findByEmail(String email) {
        return appUserRepository
                .findByEmail(email.toLowerCase(Locale.ROOT))
                .orElseThrow(() -> {
                    throw new BadRequestException("An account with that email " + email + " not found");
                });
    }

    @Override
    public AppUser availableResendConfirmEmail(String email) {
        AppUser appUser = findByEmail(email);
        Role roleNew = roleService.findByName(Roles.NEW);
        if (!appUser
                .getRoles()
                .contains(roleNew)) {
            throw new BadRequestException("Email already verified");
        }
        if (!appUser
                .getLastActionTime()
                .plusMinutes(regProperties
                                     .getEmail()
                                     .getResendTimeoutInMin())
                .isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Activation email has already been sent, please wait " + regProperties
                    .getEmail()
                    .getResendTimeoutInMin() + " minutes before sending a new one");
        }
        appUser.setLastActionTime(LocalDateTime.now());
        return appUserRepository.save(appUser);
    }
}
