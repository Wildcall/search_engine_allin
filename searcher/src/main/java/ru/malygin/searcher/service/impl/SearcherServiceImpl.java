package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.config.ResourceConfig;
import ru.malygin.searcher.model.ResourceType;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.searcher.Searcher;
import ru.malygin.searcher.searcher.util.AlphabetType;
import ru.malygin.searcher.searcher.util.Lemmantisator;
import ru.malygin.searcher.service.IndexService;
import ru.malygin.searcher.service.LemmaService;
import ru.malygin.searcher.service.PageService;
import ru.malygin.searcher.service.SearcherService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.malygin.searcher.exception.ResponseException.fluxResponseNoContent;
import static ru.malygin.searcher.exception.ResponseException.monoResponseBadRequest;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SearcherServiceImpl implements SearcherService {

    private final Map<Task, Searcher> currentRunningTasks = new ConcurrentHashMap<>();
    private final PageService pageService;
    private final LemmaService lemmaService;
    private final IndexService indexService;
    private final Searcher.Builder builder;
    private final ResourceConfig resourceConfig;

    @Override
    public Mono<String> start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null) {
            log.warn("SEARCHER / Code: 4001");
            return monoResponseBadRequest("4001");
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            log.warn("SEARCHER / Code: 4002");
            return monoResponseBadRequest("4002");
        }

        if (resourceNotAvailable(ResourceType.CRAWLER)) {
            log.warn("SEARCHER / Code: 4003");
            return monoResponseBadRequest("4003");}

        if (resourceNotAvailable(ResourceType.INDEXER)) {
            log.warn("SEARCHER / Code: 4006");
            return monoResponseBadRequest("4006");}
        //  @formatter:on

        return pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .then(lemmaService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .then(indexService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .doOnSuccess(sink -> {
                    Searcher searcher = builder.build();
                    searcher.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, searcher);
                    log.info("SEARCHER / Action: start / TaskId: {} / Path: {} / SiteId: {} / AppUserId: {}",
                             task.getId(),
                             task.getPath(), task.getSiteId(), task.getAppUserId());
                })
                .then(Mono.just("OK"));
    }

    @Override
    public Mono<String> stop(Task task) {
        Searcher searcher = currentRunningTasks.get(task);
        if (searcher == null) {
            log.warn("SEARCHER / Code: 4004");
            return monoResponseBadRequest("4004");
        }
        searcher.stop();
        currentRunningTasks.remove(task);
        log.info("SEARCHER / Action: stop / TaskId: {} / Path: {} / SiteId: {} / AppUserId: {}", task.getId(),
                 task.getPath(), task.getSiteId(), task.getAppUserId());
        return Mono.just("OK");
    }


    @Override
    public Flux<SearchResponse> search(Long siteId,
                                       String query) {
        List<String> words = Lemmantisator.getLemmas(query, AlphabetType.CYRILLIC);
        return pageService
                .search(siteId, words)
                .switchIfEmpty(fluxResponseNoContent("no pages"));
    }

    private boolean resourceNotAvailable(ResourceType type) {
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
}
