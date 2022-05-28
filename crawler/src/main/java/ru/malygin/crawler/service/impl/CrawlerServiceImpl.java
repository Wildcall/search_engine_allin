package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.crawler.crawler.Crawler;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.model.TaskAction;
import ru.malygin.crawler.rabbit.impl.LogSender;
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
    private final LogSender logSender;

    @Override
    public void process(Task task,
                        TaskAction action) {
        if (action.equals(TaskAction.START))
            this.start(task);
        if (action.equals(TaskAction.STOP))
            this.stop(task);
    }

    private void start(Task task) {
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();

        //  @formatter:off
        if (currentRunningTasks.get(task) != null){
            logSender.send("CRAWLER / Code: 6001");
            return;
        }

        if (currentRunningTasks.keySet().stream().anyMatch(t -> t.getPath().equalsIgnoreCase(task.getPath()))) {
            logSender.send("CRAWLER / Code: 6002");
            return;
        }

        if (siteNotAvailable(task)) {
            logSender.send("CRAWLER / Code: 6003");
            return;
        }
        //  @formatter:on

        pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .doOnSuccess(v -> {
                    Crawler crawler = crawlerBuilder.build();
                    crawler.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, crawler);
                    logSender.send("CRAWLER / Action: start / TaskId: %s / Path: %s / SiteId: %s / AppUserId: %s",
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
            logSender.send("CRAWLER / Code: 6004");
            return;
        }
        crawler.stop();
        currentRunningTasks.remove(task);
        logSender.send("CRAWLER / Action: stop / TaskId: %s / Path: %s / SiteId: %s / AppUserId: %s", task.getId(),
                       task.getPath(), task.getSiteId(), task.getAppUserId());
    }

    private boolean siteNotAvailable(Task task) {
        logSender.send("PING / Resource: %s", task.getPath());
        try {
            URL url = new URL(task.getPath());
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("HEAD");
            if (huc.getResponseCode() == 200) {
                return false;
            }
        } catch (Exception e) {
            logSender.send("PING ERROR  / Resource: %s / Message: %s", task.getPath(), e.getMessage());
        }
        logSender.send("PING ERROR  / Resource: %s", task.getPath());
        return true;
    }
}
