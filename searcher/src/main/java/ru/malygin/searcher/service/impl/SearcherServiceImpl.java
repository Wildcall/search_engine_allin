package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.logsenderspringbootstarter.service.LogSender;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.model.TaskAction;
import ru.malygin.searcher.searcher.Searcher;
import ru.malygin.searcher.searcher.util.AlphabetType;
import ru.malygin.searcher.searcher.util.Lemmantisator;
import ru.malygin.searcher.service.IndexService;
import ru.malygin.searcher.service.LemmaService;
import ru.malygin.searcher.service.PageService;
import ru.malygin.searcher.service.SearcherService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.malygin.searcher.exception.ResponseException.fluxResponseNoContent;

@RequiredArgsConstructor
@Service
public class SearcherServiceImpl implements SearcherService {

    private final Map<Task, Searcher> currentRunningTasks = new ConcurrentHashMap<>();
    private final PageService pageService;
    private final LemmaService lemmaService;
    private final IndexService indexService;
    private final Searcher.Builder builder;
    private final LogSender logSender;

    @Override
    public void process(Task task,
                        TaskAction action) {
        if (action.equals(TaskAction.START))
            this.start(task);
        if (action.equals(TaskAction.STOP))
            this.stop(task);
    }

    public void start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null) {
            logSender.error("SEARCHER / Code: 4001");
            return;
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            logSender.error("SEARCHER / Code: 4002");
            return;
        }
        //  @formatter:on

        pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .then(lemmaService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .then(indexService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .doOnSuccess(sink -> {
                    Searcher searcher = builder.build();
                    searcher.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, searcher);
                    logSender.info("SEARCHER / Action: start / TaskId: {} / Path: {} / SiteId: {} / AppUserId: {}",
                                   task.getId(),
                                   task.getPath(), task.getSiteId(), task.getAppUserId());
                })
                .subscribe();
    }

    public void stop(Task task) {
        Searcher searcher = currentRunningTasks.get(task);
        if (searcher == null) {
            logSender.error("SEARCHER / Code: 4004");
            return;
        }
        searcher.stop();
        currentRunningTasks.remove(task);
        logSender.info("SEARCHER / Action: stop / TaskId: {} / Path: {} / SiteId: {} / AppUserId: {}", task.getId(),
                       task.getPath(), task.getSiteId(), task.getAppUserId());
    }


    @Override
    public Flux<SearchResponse> search(Long siteId,
                                       String query) {
        List<String> words = Lemmantisator.getLemmas(query, AlphabetType.CYRILLIC);
        return pageService
                .search(siteId, words)
                .switchIfEmpty(fluxResponseNoContent("no pages"));
    }
}
