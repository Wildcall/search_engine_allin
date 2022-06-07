package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.helper.events.TaskCallbackEvent;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.service.cross.DataReceiver;
import ru.malygin.helper.service.senders.LogSender;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;
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
    private final SearchEngineProperties properties;
    private final ApplicationEventPublisher publisher;
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

        Long pageCount = pageResourceAvailable(task);
        if (pageCount < 0) {
            publishErrorCallbackEvent(task, 6003);
            return;
        }

        Long lemmaCount = lemmaResourceAvailable(task);
        if (lemmaCount < 0) {
            publishErrorCallbackEvent(task, 6003);
            return;
        }

        Long indexCount = indexResourceAvailable(task);
        if (indexCount < 0) {
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
                    searcher.start(task, pageCount, lemmaCount, indexCount, currentRunningTasks);
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

    private Long pageResourceAvailable(Task task) {
        DataRequest dataRequest = new DataRequest(task.getSiteId(), task.getId(), task.getAppUserId());
        SearchEngineProperties.Common.Request request = properties
                .getCommon()
                .getRequest();
        return dataReceiver.requestData(dataRequest, request.getExchange(),
                                        request.getPageRoute());
    }

    private Long lemmaResourceAvailable(Task task) {
        DataRequest dataRequest = new DataRequest(task.getSiteId(), task.getId(), task.getAppUserId());
        SearchEngineProperties.Common.Request request = properties
                .getCommon()
                .getRequest();
        return dataReceiver.requestData(dataRequest, request.getExchange(),
                                        request.getLemmaRoute());
    }

    private Long indexResourceAvailable(Task task) {
        DataRequest dataRequest = new DataRequest(task.getSiteId(), task.getId(), task.getAppUserId());
        SearchEngineProperties.Common.Request request = properties
                .getCommon()
                .getRequest();
        return dataReceiver.requestData(dataRequest, request.getExchange(),
                                        request.getIndexRoute());
    }

    private void publishErrorCallbackEvent(Task task,
                                           int code) {
        logSender.error("SEARCHER ERROR / Id: %s / Code: %s", task.getId(), code);
        publisher.publishEvent(new TaskCallbackEvent(new TaskCallback(task.getId(), TaskState.ERROR, null, null)));
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
