package ru.malygin.registration.exception;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

/**
 * Обработчик для пользовательских ошибок и ошибок валидации
 */

@Slf4j
@Component
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             @NonNull WebRequest request) {
        if (ex instanceof MethodArgumentNotValidException e) {
            String error = "Validation failed";
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error);
            List<ApiValidationError> validationErrors = e
                    .getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> new ApiValidationError(fieldError.getObjectName(), fieldError.getField(),
                                                              fieldError.getRejectedValue(),
                                                              fieldError.getDefaultMessage()))
                    .toList();
            apiError.setSubErrors(validationErrors);
            return new ResponseEntity<>(apiError, apiError.getStatus());
        }
        return new ResponseEntity<>(new ApiError(status), status);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(
            BadRequestException ex) {
        log.error("Error logging in: {}", ex.getMessage());
        return ex.getResponse();
    }
}
