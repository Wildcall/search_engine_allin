package ru.malygin.searcher.searcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.searcher.config.ResourceConfig;
import ru.malygin.searcher.config.WebClientConfiguration;
import ru.malygin.searcher.model.ResourceType;
import ru.malygin.searcher.model.entity.impl.Index;
import ru.malygin.searcher.model.entity.impl.Lemma;
import ru.malygin.searcher.model.entity.impl.Page;
import ru.malygin.searcher.security.JwtUtil;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class Fetcher {

    private final ResourceConfig resourceConfig;
    private final WebClientConfiguration webClientConfiguration;
    private final JwtUtil jwtUtil;

    public Flux<Page> fetchPages(Long siteId,
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

    public Flux<Lemma> fetchLemmas(Long siteId,
                                   Long appUserId) {
        ResourceConfig.ResourceParam resourceParam = resourceConfig.getResource(ResourceType.INDEXER);

        String uri = resourceParam.getBaseUrl() + resourceParam
                .getAvailablePaths()
                .get("lemma") + "?siteId=" + siteId + "&appUserId=" + appUserId;

        return webClientConfiguration
                .getWebClient()
                .get()
                .uri(uri)
                .header(AUTHORIZATION, "Bearer " + jwtUtil.generateResourceToken(ResourceType.INDEXER))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Lemma.class))
                .doOnError(throwable -> log.error("FETCH LEMMA / Error: {}", throwable.getMessage()))
                .onErrorReturn(new Lemma());
    }

    public Flux<Index> fetchIndexes(Long siteId,
                                    Long appUserId) {
        ResourceConfig.ResourceParam resourceParam = resourceConfig.getResource(ResourceType.INDEXER);

        String uri = resourceParam.getBaseUrl() + resourceParam
                .getAvailablePaths()
                .get("index") + "?siteId=" + siteId + "&appUserId=" + appUserId;

        return webClientConfiguration
                .getWebClient()
                .get()
                .uri(uri)
                .header(AUTHORIZATION, "Bearer " + jwtUtil.generateResourceToken(ResourceType.INDEXER))
                .accept(MediaType.APPLICATION_NDJSON)
                .exchangeToFlux(clientResponse -> clientResponse.bodyToFlux(Index.class))
                .doOnError(throwable -> log.error("FETCH PAGE / Error: {}", throwable.getMessage()))
                .onErrorReturn(new Index());
    }
}
