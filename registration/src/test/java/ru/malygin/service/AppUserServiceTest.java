package ru.malygin.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.malygin.repository.AppUserRepository;
import ru.malygin.service.impl.AppUserServiceImpl;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AppUserServiceTest {

    @Mock
    private RoleService mockedRoleService;

    @Mock
    private AppUserRepository mockedAppUserRepository;

    @Mock
    private PasswordEncoder mockedPasswordEncoder;

    @InjectMocks
    private AppUserServiceImpl underTest;

    @Test
    @Disabled
    void loadUserByUsername() {
    }

    @Test
    @Disabled
    void save() {
    }

    @Test
    @Disabled
    void confirmEmail() {
    }

    @Test
    @Disabled
    void findByEmail() {
    }

    @Test
    @Disabled
    void availableResendConfirmEmail() {
    }
}