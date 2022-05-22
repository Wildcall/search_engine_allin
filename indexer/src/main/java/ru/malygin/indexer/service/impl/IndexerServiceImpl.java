package ru.malygin.indexer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.malygin.indexer.config.ResourceConfig;
import ru.malygin.indexer.indexer.Indexer;
import ru.malygin.indexer.model.ResourceType;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.service.IndexService;
import ru.malygin.indexer.service.LemmaService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.malygin.indexer.exception.ResponseException.monoResponseBadRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndexerServiceImpl implements ru.malygin.indexer.service.IndexerService {

    private final Map<Task, Indexer> currentRunningTasks = new ConcurrentHashMap<>();
    private final LemmaService lemmaService;
    private final IndexService indexService;
    private final Indexer.Builder indexerBuilder;
    private final ResourceConfig resourceConfig;

    @Override
    public Mono<String> start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null)
            return monoResponseBadRequest("5001");

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath())))
            return monoResponseBadRequest("5002");

        if (resourceNotAvailable(ResourceType.CRAWLER))
            return monoResponseBadRequest("5003");
        //  @formatter:on

        return lemmaService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .then(indexService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .doOnSuccess(sink -> {
                    Indexer indexer = indexerBuilder.build();
                    indexer.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, indexer);
                })
                .then(Mono.just("OK"));
    }

    @Override
    public Mono<String> stop(Task task) {
        Indexer indexer = currentRunningTasks.get(task);
        if (indexer == null) return monoResponseBadRequest("5004");
        indexer.stop();
        currentRunningTasks.remove(task);
        return Mono.just("OK");
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
