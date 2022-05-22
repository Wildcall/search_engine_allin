package ru.malygin.indexer.indexer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.indexer.config.ResourceConfig;
import ru.malygin.indexer.config.WebClientConfiguration;
import ru.malygin.indexer.model.Page;
import ru.malygin.indexer.model.ResourceType;
import ru.malygin.indexer.security.JwtUtil;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageFetcher {

    private final ResourceConfig resourceConfig;
    private final WebClientConfiguration webClientConfiguration;
    private final JwtUtil jwtUtil;

    public Flux<Page> fetch(Long siteId,
                            Long appUserId) {
        ResourceConfig.ResourceParam resourceParam = resourceConfig.getResource(ResourceType.CRAWLER);

        String uri = resourceParam.getBaseUrl() + resourceParam
                .getAvailablePaths()
                .get("page") + "?siteId=" + siteId + "&appUserId=" + appUserId;

        return webClientConfiguration
                .getWebClient()
                .get()
                .uri(uri)
                .header(AUTHORIZATION, "Bearer " + jwtUtil.generateResourceToken(ResourceType.CRAWLER))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Page.class))
                .doOnError(throwable -> log.error("FETCH PAGE / Error: {}", throwable.getMessage()))
                .onErrorReturn(new Page());
    }
}
