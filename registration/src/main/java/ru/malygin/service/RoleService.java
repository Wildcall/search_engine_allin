package ru.malygin.service;

import ru.malygin.model.entity.Role;

public interface RoleService {

    Role save(Role role);

    Role findByName(String name);
}
