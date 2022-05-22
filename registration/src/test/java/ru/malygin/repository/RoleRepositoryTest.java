package ru.malygin.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.malygin.model.entity.Role;

import java.util.Optional;

@DataJpaTest
class RoleRepositoryTest {

    private final String existName = "TEST";
    private final String notExistName = "NOT";

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role role = new Role(null, existName);

        roleRepository.save(role);
    }

    @AfterEach
    void tearDown() {
        roleRepository.deleteAll();
    }

    @Test
    void existsByName_ReturnTrue_IfExist() {
        boolean exists = roleRepository.existsByName(existName);

        Assertions
                .assertThat(exists)
                .isTrue();
    }

    @Test
    void existsByName_ReturnFalse_IfNotExist() {
        boolean notExist = roleRepository.existsByName(notExistName);

        Assertions
                .assertThat(notExist)
                .isFalse();
    }

    @Test
    void findByName_ReturnOptional() {
        Optional<Role> exist = roleRepository.findByName(existName);

        Assertions
                .assertThat(exist)
                .isNotNull()
                .isInstanceOf(Optional.class);
    }

    @Test
    void findByName_OptionalContainRole_IfExist() {
        Role exist = roleRepository
                .findByName(existName)
                .orElse(null);

        Assertions
                .assertThat(exist)
                .isNotNull();
    }

    @Test
    void findByName_OptionalContainNull_IfNotExist() {
        Role exist = roleRepository
                .findByName(notExistName)
                .orElse(null);

        Assertions
                .assertThat(exist)
                .isNull();
    }
}