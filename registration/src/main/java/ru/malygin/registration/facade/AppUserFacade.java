package ru.malygin.registration.facade;

import org.springframework.security.core.Authentication;
import ru.malygin.registration.model.AuthResponse;
import ru.malygin.registration.model.dto.AppUserDto;

import java.util.Map;

public interface AppUserFacade {
    /**
     * Сохраняет пользователя в базу данных, и отправляет уведомление на почту указанную пользователем.
     *
     * @param appUserDto
     * @return AppUserDto
     */
    AuthResponse save(AppUserDto appUserDto);

    /**
     * Повторно отправляет уведомление для авторизированного пользователя, если
     * с последней отправки прошло больше времени чем указанно в email.resend
     *
     * @param authentication
     * @return AppUserDto
     */
    String resendConfirmEmail(Authentication authentication);

    /**
     * Обновляет токен доступа для авторизированного пользователя
     *
     * @param authentication
     * @return Map<String, String>
     */
    Map<String, String> refreshAccessToken(Authentication authentication);

    /**
     * Подтверждает почту пользователя и посылает уведомление об успешной регистрации
     *
     * @param authentication
     * @return AppUserDto
     */
    String confirmEmail(Authentication authentication);

    /**
     * Возвращает данные о пользователе
     *
     * @param authentication
     * @return AppUserDto
     */
    AppUserDto getUserData(Authentication authentication);
}
