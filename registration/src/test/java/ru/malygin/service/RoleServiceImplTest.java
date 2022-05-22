package ru.malygin.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.malygin.model.entity.Role;
import ru.malygin.repository.RoleRepository;
import ru.malygin.service.impl.RoleServiceImpl;

import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class RoleServiceImplTest {

    private final Role existRole = new Role(1L, "EXIST");
    private final Role notExistRole = new Role(2L, "NOT_EXIST");

    @Mock
    private RoleRepository mockedRoleRepository;

    @InjectMocks
    private RoleServiceImpl underTest;

    @Test
    @DisplayName("Returns the role if a name is exist")
    void findByName_Role_ExistName() {
        Mockito
                .when(mockedRoleRepository.findByName(existRole.getName()))
                .thenReturn(Optional.of(existRole));

        Role result = underTest.findByName(existRole.getName());
        Assertions
                .assertThat(result)
                .isEqualTo(existRole);
    }

    @Test
    @DisplayName("Returns an IllegalArgumentException if a name not exist")
    void findByName_IllegalArgumentException_NotExistName() {
        Mockito
                .when(mockedRoleRepository.findByName(notExistRole.getName()))
                .thenReturn(Optional.empty());

        try {
            underTest.findByName(notExistRole.getName());
        } catch (Exception e) {
            Assertions
                    .assertThat(e)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Role with name: " + notExistRole.getName() + " not found");
        }
    }

    @Test
    @DisplayName("Returns the role if the name is not already taken")
    void save_Role_NameNotAlreadyTaken() {
        Mockito
                .when(mockedRoleRepository.existsByName(notExistRole.getName()))
                .thenReturn(false);
        Mockito
                .when(mockedRoleRepository.save(notExistRole))
                .thenReturn(notExistRole);

        Role role = underTest.save(notExistRole);
        Assertions
                .assertThat(role)
                .isEqualTo(notExistRole);
    }

    @Test
    @DisplayName("Returns an IllegalArgumentException if the name is already taken")
    void save_IllegalArgumentException_NameAlreadyTaken() {
        Mockito
                .when(mockedRoleRepository.existsByName(existRole.getName()))
                .thenReturn(true);
        try {
            underTest.save(existRole);
        } catch (Exception e) {
            Assertions
                    .assertThat(e)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Role with name: " + existRole.getName() + " already taken");
        }
    }
}