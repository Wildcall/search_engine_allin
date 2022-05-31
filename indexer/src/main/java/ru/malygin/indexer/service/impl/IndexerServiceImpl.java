package ru.malygin.indexer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskReceiveEvent;
import ru.malygin.helper.service.LogSender;
import ru.malygin.indexer.config.RabbitConfig;
import ru.malygin.indexer.indexer.Indexer;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.service.IndexService;
import ru.malygin.indexer.service.IndexerService;
import ru.malygin.indexer.service.LemmaService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndexerServiceImpl implements IndexerService {

    private final Map<Task, Indexer> currentRunningTasks = new ConcurrentHashMap<>();
    private final LemmaService lemmaService;
    private final IndexService indexService;
    private final Indexer.Builder indexerBuilder;
    private final LogSender logSender;
    private final RabbitConfig.Client client;

    @Override
    public void process(TaskReceiveEvent<Task> event) {
        Task task = event.getTask();
        TaskAction action = event.getAction();

        if (action.equals(TaskAction.START)) {
            client.send(task.getAppUserId(), task.getSiteId(), task.getId());
//            this.start(task);
            return;
        }
        if (action.equals(TaskAction.STOP)) {
//            this.stop(task);
            return;
        }
        if (action.equals(TaskAction.UNKNOWN)) {
            logSender.error("CRAWLER / Task action unknown: %s", action);
        }
    }

    private void start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null) {
            logSender.error("INDEXER / Code: 5001");
            return;
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            logSender.error("INDEXER / Code: 6002");
            return;
        }
        //  @formatter:on

        lemmaService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .then(indexService.deleteAllBySiteIdAndAppUserId(siteId, appUserId))
                .doOnSuccess(sink -> {
                    Indexer indexer = indexerBuilder.build();
                    indexer.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, indexer);
                    logSender.info("INDEXER / Action: start / TaskId: %s / Path: %s / SiteId: %s / AppUserId: %s",
                                   task.getId(), task.getPath(), task.getSiteId(), task.getAppUserId());
                })
                .subscribe();
    }

    private void stop(Task task) {
        Indexer indexer = currentRunningTasks.get(task);
        if (indexer == null) {
            logSender.error("INDEXER / Code: 5004");
            return;
        }
        indexer.stop();
        currentRunningTasks.remove(task);
        logSender.info("INDEXER / Action: stop / TaskId: %s / Path: %s / SiteId: %s / AppUserId: %s", task.getId(),
                       task.getPath(), task.getSiteId(), task.getAppUserId());
    }
}
