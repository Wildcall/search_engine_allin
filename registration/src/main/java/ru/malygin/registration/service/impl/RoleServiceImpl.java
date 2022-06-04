package ru.malygin.registration.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.registration.model.entity.Role;
import ru.malygin.registration.repository.RoleRepository;
import ru.malygin.registration.service.RoleService;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        if (roleRepository.existsByName(role.getName()))
            throw new IllegalArgumentException("Role with name: " + role.getName() + " already taken");
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Role with name: " + name + " not found");
                });
    }
}
