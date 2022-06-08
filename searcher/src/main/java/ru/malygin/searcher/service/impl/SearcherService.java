package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.senders.LogSender;
import ru.malygin.helper.senders.TaskCallbackSender;
import ru.malygin.helper.service.DataReceiver;
import ru.malygin.helper.service.NodeMainService;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.searcher.Searcher;
import ru.malygin.searcher.searcher.util.AlphabetType;
import ru.malygin.searcher.searcher.util.Lemmantisator;
import ru.malygin.searcher.service.IndexService;
import ru.malygin.searcher.service.LemmaService;
import ru.malygin.searcher.service.PageService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.malygin.searcher.exception.ResponseException.fluxResponseNoContent;

@RequiredArgsConstructor
@Service
public class SearcherService implements NodeMainService<Task> {

    private final Map<Task, Searcher> currentRunningTasks = new ConcurrentHashMap<>();
    private final PageService pageService;
    private final LemmaService lemmaService;
    private final IndexService indexService;
    private final Searcher.Builder builder;
    private final LogSender logSender;
    private final TaskCallbackSender taskCallbackSender;
    private final DataReceiver dataReceiver;

    public void start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null) {
            publishErrorCallbackEvent(task, 6001);
            return;
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            publishErrorCallbackEvent(task, 6002);
            return;
        }

        DataRequest pageDataRequest = dataReceiver.createPageDataRequest(task.getId(),
                                                                         task.getSiteId(),
                                                                         task.getAppUserId());
        if (!dataReceiver.requestData(pageDataRequest)) {
            publishErrorCallbackEvent(task, 6003);
            return;
        }

        DataRequest lemmaDataRequest = dataReceiver.createLemmaDataRequest(task.getId(),
                                                                         task.getSiteId(),
                                                                         task.getAppUserId());
        if (!dataReceiver.requestData(lemmaDataRequest)) {
            publishErrorCallbackEvent(task, 6003);
            return;
        }

        DataRequest indexDataRequest = dataReceiver.createIndexDataRequest(task.getId(),
                                                                          task.getSiteId(),
                                                                          task.getAppUserId());
        if (!dataReceiver.requestData(indexDataRequest)) {
            publishErrorCallbackEvent(task, 6003);
            return;
        }
        //  @formatter:on

        pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .then(lemmaService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .then(indexService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .doOnSuccess(sink -> {
                    Searcher searcher = builder.build();
                    searcher.start(task, pageDataRequest, lemmaDataRequest, indexDataRequest, currentRunningTasks);
                    currentRunningTasks.put(task, searcher);
                })
                .subscribe();
    }

    public void stop(Task task) {
        Searcher searcher = currentRunningTasks.get(task);
        if (searcher == null) {
            publishErrorCallbackEvent(task, 6004);
            return;
        }
        searcher.stop();
        currentRunningTasks.remove(task);
    }

    private void publishErrorCallbackEvent(Task task,
                                           int code) {
        logSender.error("SEARCHER ERROR / Id: %s / Code: %s", task.getId(), code);
        TaskCallback callback = new TaskCallback(task.getId(), TaskState.ERROR, null, null);
        taskCallbackSender.send(callback);
    }

    public Flux<SearchResponse> search(Long siteId,
                                       String query) {
        List<String> words = Lemmantisator.getLemmas(query, AlphabetType.CYRILLIC);
        return pageService
                .search(siteId, words)
                .switchIfEmpty(fluxResponseNoContent("no pages"));
    }
}
