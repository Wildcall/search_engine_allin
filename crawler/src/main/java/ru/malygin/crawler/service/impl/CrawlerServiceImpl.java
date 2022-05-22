package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.malygin.crawler.crawler.Crawler;
import ru.malygin.crawler.exception.ResponseException;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.service.CrawlerService;
import ru.malygin.crawler.service.PageService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class CrawlerServiceImpl implements CrawlerService {

    private final Map<Task, Crawler> currentRunningTasks = new ConcurrentHashMap<>();
    private final PageService pageService;
    private final Crawler.Builder crawlerBuilder;

    @Override
    public Mono<String> start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null){
            log.warn("CRAWLER / Code: 6001");
            return ResponseException.monoResponseBadRequest("6001");
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            log.warn("CRAWLER / Code: 6002");
            return ResponseException.monoResponseBadRequest("6002");
        }


        if (siteNotAvailable(task)) {
            log.warn("CRAWLER / Code: 6003");
            return ResponseException.monoResponseBadRequest("6003");
        }
        //  @formatter:on

        return pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .doOnSuccess(v -> {
                    Crawler crawler = crawlerBuilder.build();
                    crawler.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, crawler);
                    log.info("CRAWLER / Action: start / TaskId: {} / Path: {} / SiteId: {} / AppUserId: {}",
                             task.getId(),
                             task.getPath(), task.getSiteId(), task.getAppUserId());
                })
                .then(Mono.just("OK"));
    }

    @Override
    public Mono<String> stop(Task task) {
        Crawler crawler = currentRunningTasks.get(task);
        if (crawler == null) {
            log.warn("CRAWLER / Code: 6004");
            return ResponseException.monoResponseBadRequest("6004");
        }
        crawler.stop();
        currentRunningTasks.remove(task);
        log.info("CRAWLER / Action: stop / TaskId: {} / Path: {} / SiteId: {} / AppUserId: {}", task.getId(),
                 task.getPath(), task.getSiteId(), task.getAppUserId());
        return Mono.just("OK");
    }

    private boolean siteNotAvailable(Task task) {
        log.info("PING / Resource: {}", task.getPath());
        try {
            URL url = new URL(task.getPath());
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            if (huc.getResponseCode() == 200) {
                return false;
            }
        } catch (Exception e) {
            log.error("PING ERROR  / Resource: {} / Message: {}", task.getPath(), e.getMessage());
        }
        log.warn("PING ERROR  / Resource: {}", task.getPath());
        return true;
    }
}
