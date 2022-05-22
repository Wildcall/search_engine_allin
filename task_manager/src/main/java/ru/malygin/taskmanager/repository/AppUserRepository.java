package ru.malygin.taskmanager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.malygin.taskmanager.model.entity.impl.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
    Boolean existsByEmail(String email);
}
