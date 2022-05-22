package ru.malygin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.malygin.model.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
