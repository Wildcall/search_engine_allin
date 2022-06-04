package ru.malygin.registration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.malygin.registration.model.Roles;
import ru.malygin.registration.model.entity.Role;
import ru.malygin.registration.service.RoleService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class InitialData {
    //  @formatter:off
    private final RoleService roleService;
    private final List<Role> roles = List.of(new Role(null, Roles.USER),
                                             new Role(null, Roles.ADMIN),
                                             new Role(null, Roles.NEW));
    //  @formatter:on

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        roles.forEach(role -> {
            try {
                Role savedRole = roleService.save(role);
                log.warn(savedRole + " saved");
            } catch (IllegalArgumentException exist) {
                log.warn(exist.getMessage());
            }
        });
    }
}
