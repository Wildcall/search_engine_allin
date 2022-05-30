package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.crawler.crawler.Crawler;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.service.CrawlerService;
import ru.malygin.crawler.service.PageService;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.helper.model.TaskReceiveEvent;
import ru.malygin.helper.service.LogSender;

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
    private final LogSender logSender;

    @Override
    public void process(TaskReceiveEvent<Task> event) {
        Task task = event.getTask();
        TaskAction action = event.getAction();

        if (action.equals(TaskAction.START)) {
            this.start(task);
            return;
        }
        if (action.equals(TaskAction.STOP)) {
            this.stop(task);
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
            logSender.error("CRAWLER / Code: 6001");
            return;
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            logSender.error("CRAWLER / Code: 6002");
            return;
        }

        if (siteNotAvailable(task)) {
            logSender.error("CRAWLER / Code: 6003");
            return;
        }
        //  @formatter:on

        pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .doOnSuccess(v -> {
                    Crawler crawler = crawlerBuilder.build();
                    crawler.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, crawler);
                    logSender.info("CRAWLER / Action: start / TaskId: %s / Path: %s / SiteId: %s / AppUserId: %s",
                                   task.getId(),
                                   task.getPath(),
                                   task.getSiteId(),
                                   task.getAppUserId());
                })
                .subscribe();
    }

    private void stop(Task task) {
        Crawler crawler = currentRunningTasks.get(task);
        if (crawler == null) {
            logSender.error("CRAWLER / Code: 6004");
            return;
        }
        crawler.stop();
        currentRunningTasks.remove(task);
        logSender.info("CRAWLER / Action: stop / TaskId: %s / Path: %s / SiteId: %s / AppUserId: %s", task.getId(),
                       task.getPath(), task.getSiteId(), task.getAppUserId());
    }

    private boolean siteNotAvailable(Task task) {
        logSender.info("PING / Resource: %s", task.getPath());
        try {
            URL url = new URL(task.getPath());
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            if (huc.getResponseCode() == 200) {
                return false;
            }
        } catch (Exception e) {
            logSender.error("PING ERROR  / Resource: %s / Message: %s", task.getPath(), e.getMessage());
        }
        logSender.error("PING ERROR  / Resource: %s", task.getPath());
        return true;
    }
}
