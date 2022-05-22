package ru.malygin.searcher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ResponseException {

    public static <T> Flux<T> fluxResponseNoContent(String message) {
        return Flux.error(new ResponseStatusException(HttpStatus.NO_CONTENT, message));
    }

    public static <T> Mono<T> monoResponseBadRequest(String message) {
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, message));
    }
}
