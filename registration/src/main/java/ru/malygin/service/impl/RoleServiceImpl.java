package ru.malygin.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.model.entity.Role;
import ru.malygin.repository.RoleRepository;
import ru.malygin.service.RoleService;

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
