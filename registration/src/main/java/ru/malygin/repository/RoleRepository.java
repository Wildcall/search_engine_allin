package ru.malygin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.malygin.model.entity.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
