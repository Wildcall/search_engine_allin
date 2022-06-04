package ru.malygin.registration.repository;

import org.springframework.data.repository.CrudRepository;
import ru.malygin.registration.model.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);

    boolean existsByEmail(String email);
}
