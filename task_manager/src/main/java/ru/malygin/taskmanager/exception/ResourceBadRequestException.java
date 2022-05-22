package ru.malygin.taskmanager.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ResourceBadRequestException extends RuntimeException {

    private final ApiError apiError;

    public ResourceBadRequestException(String message) {
        super(message);
        this.apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public ResponseEntity<Object> getResponse() {
        return new ResponseEntity<>(this.apiError, this.apiError.getStatus());
    }
}
