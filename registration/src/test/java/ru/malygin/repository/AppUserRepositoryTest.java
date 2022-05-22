package ru.malygin.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.malygin.model.entity.AppUser;
import ru.malygin.model.entity.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
class AppUserRepositoryTest {

    private final String existEmail = "exist@email.com";
    private final String notExistEmail = "notexist@email.com";

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        AppUser appUser = new AppUser(null, "password", existEmail, true, LocalDateTime.now(), LocalDateTime.now(),
                                      List.of(new Role(1L, "NEW")));

        appUserRepository.save(appUser);
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
    }

    @Test
    void existsByEmail_ReturnTrue_IfExist() {
        boolean exist = appUserRepository.existsByEmail(existEmail);

        Assertions
                .assertThat(exist)
                .isTrue();
    }

    @Test
    void existsByEmail_ReturnFalse_IfNotExist() {
        boolean notExist = appUserRepository.existsByEmail(notExistEmail);

        Assertions
                .assertThat(notExist)
                .isFalse();
    }

    @Test
    void findByEmail_ReturnOptional() {
        Optional<AppUser> exist = appUserRepository.findByEmail(existEmail);

        Assertions
                .assertThat(exist)
                .isNotNull()
                .isInstanceOf(Optional.class);
    }

    @Test
    void findByEmail_OptionalContainAppUser_IfExist() {
        AppUser exist = appUserRepository
                .findByEmail(existEmail)
                .orElse(null);

        Assertions
                .assertThat(exist)
                .isNotNull();
    }

    @Test
    void findByEmail_OptionalContainNull_IfNotExist() {
        AppUser exist = appUserRepository
                .findByEmail(notExistEmail)
                .orElse(null);

        Assertions
                .assertThat(exist)
                .isNull();
    }
}