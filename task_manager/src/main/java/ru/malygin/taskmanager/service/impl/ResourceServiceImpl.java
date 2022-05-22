package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import ru.malygin.taskmanager.config.ResourceConfig;
import ru.malygin.taskmanager.config.WebClientConfiguration;
import ru.malygin.taskmanager.exception.ResourceBadRequestException;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.security.JwtUtil;
import ru.malygin.taskmanager.service.ResourceService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResourceServiceImpl implements ResourceService {

    private final WebClientConfiguration webClientConfiguration;
    private final ResourceConfig resourceConfig;
    private final JwtUtil jwtUtil;

    @Override
    public boolean start(Task task) {
        String response = sendTask("start", task.getType(), task.toBody());
        return response.equals("OK");
    }

    @Override
    public boolean stop(Task task) {
        String response = sendTask("stop", task.getType(), task.toBody());
        return response.equals("OK");
    }

    @Override
    public boolean resourceNotAvailable(ResourceType type) {
        ResourceConfig.ResourceParam param = resourceConfig.getResource(type);

        String uri = param.getBaseUrl();

        log.info("PING / Resource: {}", uri);
        try {
            URL url = new URL(uri);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            if (huc.getResponseCode() == 200) {
                return false;
            }
        } catch (Exception e) {
            log.error("PING ERROR  / Resource: {} / Message: {}", uri, e.getMessage());
        }
        return true;
    }

    private String sendTask(String pathKey,
                            ResourceType type,
                            Map<String, Object> body) {
        ResourceConfig.ResourceParam resourceParam = resourceConfig.getResource(type);
        String uri = resourceParam.getBaseUrl() + resourceParam
                .getAvailablePaths()
                .get(pathKey);
        try {
            log.info("RESOURCE REQUEST / Resource: {} / Action: {} / Uri: {}", type, pathKey, uri);
            return webClientConfiguration
                    .getWebClient()
                    .post()
                    .uri(uri)
                    .header(AUTHORIZATION, "Bearer " + jwtUtil.generateResourceToken(type))
                    .body(BodyInserters.fromValue(body))
                    .exchangeToMono(clientResponse -> {
                        //  @formatter:off
                        if (clientResponse.statusCode().is2xxSuccessful())
                            return clientResponse
                                    .bodyToMono(String.class)
                                    .doOnSuccess(m -> log.info(
                                            "RESOURCE RESPONSE / Resource: {} / Action: {} / Uri: {} / Response: {}", type,
                                            pathKey, uri, m));
                        else
                            return clientResponse
                                    .bodyToMono(String.class)
                                    .doOnSuccess(m -> log.error(
                                            "RESOURCE RESPONSE ERROR / Resource: {} / Action: {} / Uri: {} / Response: {}", type,
                                            pathKey, uri, m));
                        //  @formatter:on
                    })
                    .block();
        } catch (Exception e) {
            log.error("Error logging: {}", e.getMessage());
            throw new ResourceBadRequestException("Unexpected error, please try again later");
        }
    }
}
