package ru.malygin.registration.service;

import ru.malygin.registration.model.entity.Role;

public interface RoleService {

    Role save(Role role);

    Role findByName(String name);
}
