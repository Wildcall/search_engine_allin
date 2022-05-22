package ru.malygin.searcher.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

import static org.springframework.boot.web.error.ErrorAttributeOptions.*;

@Component
@Order(-2)
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(DefaultErrorAttributes defaultErrorAttributes,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer) {
        super(defaultErrorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest serverRequest) {
        String query = serverRequest
                .uri()
                .getQuery();
        ErrorAttributeOptions errorAttributeOptions =
                isTraceEnabled(query)
                        ? of(Include.STACK_TRACE)
                        : defaults();

        Map<String, Object> errorAttributesMap = getErrorAttributesMap(serverRequest, errorAttributeOptions);
        int status = (int) Optional
                .ofNullable(errorAttributesMap.get("status"))
                .orElse(500);
        return ServerResponse
                .status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(errorAttributesMap));
    }

    private Map<String, Object> getErrorAttributesMap(ServerRequest serverRequest,
                                                   ErrorAttributeOptions errorAttributeOptions) {
        Map<String, Object> errorAttributesMap = getErrorAttributes(serverRequest, errorAttributeOptions);
        Throwable throwable = getError(serverRequest);
        if (throwable instanceof ResponseStatusException ex) {
            errorAttributesMap.put("message", ex.getMessage());
        }
        return errorAttributesMap;
    }


    private boolean isTraceEnabled(String query) {
        return query != null && query.contains("trace=true");
    }
}
