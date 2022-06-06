package ru.malygin.crawler.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ru.malygin.crawler.crawler.Crawler;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.service.CrawlerService;
import ru.malygin.crawler.service.PageService;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.model.TaskCallbackEvent;
import ru.malygin.helper.model.TaskState;
import ru.malygin.helper.service.senders.LogSender;

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
    private final ApplicationEventPublisher publisher;

    @Override
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

        if (siteNotAvailable(task)) {
            publishErrorCallbackEvent(task, 6003);
            return;
        }
        //  @formatter:on

        pageService
                .deleteAllBySiteIdAndAppUserId(siteId, appUserId)
                .doOnSuccess(v -> {
                    Crawler crawler = crawlerBuilder.build();
                    crawler.start(task, currentRunningTasks);
                    currentRunningTasks.put(task, crawler);
                })
                .subscribe();
    }

    @Override
    public void stop(Task task) {
        Crawler crawler = currentRunningTasks.get(task);
        if (crawler == null) {
            publishErrorCallbackEvent(task, 6004);
            return;
        }
        crawler.stop();
        currentRunningTasks.remove(task);
    }

    private void publishErrorCallbackEvent(Task task,
                                           int code) {
        logSender.error("CRAWLER ERROR / Id: %s / Code: %s", task.getId(), code);
        publisher.publishEvent(new TaskCallbackEvent(new TaskCallback(task.getId(), TaskState.ERROR, null, null)));
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
