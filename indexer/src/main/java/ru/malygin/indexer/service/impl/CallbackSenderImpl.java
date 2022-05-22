package ru.malygin.indexer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import ru.malygin.indexer.config.ResourceConfig;
import ru.malygin.indexer.config.WebClientConfiguration;
import ru.malygin.indexer.model.ResourceCallback;
import ru.malygin.indexer.model.ResourceType;
import ru.malygin.indexer.security.JwtUtil;
import ru.malygin.indexer.service.CallbackSender;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class CallbackSenderImpl implements CallbackSender {

    private final WebClientConfiguration webClientConfiguration;
    private final ResourceConfig resourceConfig;
    private final JwtUtil jwtUtil;

    @Override
    public void onApplicationEvent(ResourceCallback event) {
        send(event);
    }

    private void send(ResourceCallback resourceCallback) {
        ResourceType type = ResourceType.TASK_MANAGER;
        ResourceConfig.ResourceParam resourceParam = resourceConfig.getResource(type);

        String uri = resourceParam.getBaseUrl() + resourceParam
                .getAvailablePaths()
                .get("callback");
        try {
            log.info("CALLBACK SEND / Resource: {} / Uri: {} / TaskId: {}", type, uri, resourceCallback.getId());
            webClientConfiguration
                    .getWebClient()
                    .post()
                    .uri(uri)
                    .body(BodyInserters.fromValue(resourceCallback))
                    .header(AUTHORIZATION, "Bearer " + jwtUtil.generateResourceToken(ResourceType.TASK_MANAGER))
                    .exchangeToMono(clientResponse -> {
                        //  @formatter:off
                        if (clientResponse.statusCode().is2xxSuccessful())
                            return clientResponse
                                    .bodyToMono(String.class)
                                    .doOnSuccess(m -> log.info(
                                            "CALLBACK RESPONSE / Resource: {} / Uri: {} / Response: {}", type, uri, m));
                        else
                            return clientResponse
                                    .bodyToMono(String.class)
                                    .doOnSuccess(m -> log.warn(
                                            "CALLBACK RESPONSE ERROR / Resource: {} / Uri: {} / Response: {}", type, uri, m));
                        //  @formatter:on
                    })
                    .subscribe();
        } catch (Exception e) {
            log.error("Error logging: {}", e.getMessage());
        }
    }
}
