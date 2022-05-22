package ru.malygin.crawler.crawler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.malygin.crawler.model.ResourceCallback;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.model.entity.impl.Page;
import ru.malygin.crawler.model.entity.impl.Statistic;
import ru.malygin.crawler.service.PageService;
import ru.malygin.crawler.service.StatisticService;
import ru.malygin.crawler.sse.PublisherFactory;
import ru.malygin.crawler.sse.event.CrawlerEvent;
import ru.malygin.crawler.sse.payload.CrawlerEventPayload;
import ru.malygin.crawler.sse.publisher.CrawlerEventPublisher;
import ru.malygin.crawler.sse.publisher.PageEventPublisher;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Crawler implements Runnable {

    // init in builder
    private final StatisticService statisticService;
    private final PublisherFactory publisherFactory;
    private final PageService pageService;
    // init
    private final AtomicInteger stateCode = new AtomicInteger(0);
    private final Queue<Page> pagesQueue = new ConcurrentLinkedQueue<>();
    private final Queue<String> linksQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Page> saveQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger crawlerEventId = new AtomicInteger(0);
    // components
    private PageFetcher pageFetcher;
    private PageSaver pageSaver;
    private LinkParser linkParser;
    private CrawlerEventPublisher crawlerEventPublisher;
    private PageEventPublisher pageEventPublisher;
    // init in start
    private Task task;
    private Map<Task, Crawler> currentRunningTasks;
    private Statistic statistic;
    private CrawlerEventPayload crawlerEventPayload;
    private String sitePath;
    // util
    private Long timerCounter;

    private static void timeOut100ms() {
        try {
            TimeUnit.MILLISECONDS.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(Task task,
                      Map<Task, Crawler> currentRunningTasks) {
        //  @formatter:off
        this.task = task;
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();
        this.sitePath = task.getPath();
        this.currentRunningTasks = currentRunningTasks;

        this.crawlerEventPublisher = publisherFactory.createCrawlerEventPublishers(siteId, appUserId);
        this.pageEventPublisher = publisherFactory.createPageEventPublisher(siteId, appUserId);

        this.pageFetcher = new PageFetcher(linksQueue,
                                           pagesQueue,
                                           SiteFetcher.getFromTask(task));

        this.linkParser = new LinkParser(linksQueue,
                                         pagesQueue,
                                         saveQueue);

        this.pageSaver = new PageSaver(pageService,
                                       saveQueue,
                                       siteId,
                                       appUserId,
                                       pageEventPublisher);

        this.statistic = new Statistic();
        this.statistic.setSiteId(siteId);
        this.statistic.setAppUserId(appUserId);

        this.crawlerEventPayload = new CrawlerEventPayload(task.getId(),
                                                           siteId,
                                                           appUserId,
                                                           stateCode.get(),
                                                           LocalDateTime.now(),
                                                           statistic);

        Thread thread = new Thread(this);
        thread.setName("Crawler-" + sitePath + "-" + appUserId);
        thread.start();
        //  @formatter:on
    }

    public void stop() {
        stateCode.set(4);
    }

    @Override
    public void run() {
        statistic.setStartTime(LocalDateTime.now());

        publishSseEvent();

        timerCounter = System.currentTimeMillis();

        stateCode.set(1);
        publishCallbackEvent();

        linksQueue.add(sitePath);

        pageFetcher.start();
        linkParser.start();
        pageSaver.start();

        watchDogLoop();
    }

    private void watchDogLoop() {
        while (true) {
            timeOut100ms();
            checkNormalComplete();

            if (stateCode.get() == 3 || stateCode.get() == 4) {
                pageFetcher.stop();
                linkParser.stop();
                pageSaver.stop();
                saveAndPublishFinalStat();
                break;
            }

            if (System.currentTimeMillis() - timerCounter > task.getEventFreqInMs()) {
                publishCurrentStat();
            }
        }
        currentRunningTasks.remove(task);
    }

    private void checkNormalComplete() {
        if (pagesQueue.isEmpty() && linksQueue.isEmpty() && saveQueue.isEmpty() && !pageFetcher
                .getServe()
                .get() && !linkParser
                .getServe()
                .get() && !pageSaver
                .getServe()
                .get()) {
            stateCode.set(3);
        }
    }

    private void saveAndPublishFinalStat() {
        setActualStatistics();
        statisticService
                .save(statistic)
                .doOnSuccess(sink -> {
                    crawlerEventPayload.setStatusCode(stateCode.get());
                    publishSseEvent();
                    publishCallbackEvent();
                    publisherFactory.deleteCrawlerEventPublisher(crawlerEventPublisher);
                    publisherFactory.deletePageEventPublisher(pageEventPublisher);
                })
                .subscribe();

    }

    private void publishCurrentStat() {
        crawlerEventPayload.setStatusCode(stateCode.get());
        setActualStatistics();
        publishSseEvent();
        timerCounter = System.currentTimeMillis();
    }

    private void setActualStatistics() {
        statistic.setEndTime(stateCode.get() == 4 || stateCode.get() == 3 ? LocalDateTime.now() : null);
        statistic.setSavedPages(pageSaver
                                        .getCompleteTasks()
                                        .get());
        statistic.setFetchPages(pageFetcher
                                        .getCompleteTasks()
                                        .get());
        statistic.setErrors(pageSaver
                                    .getErrorsCount()
                                    .get());
        statistic.setLinksCount(linkParser
                                        .getCompleteTasks()
                                        .get());
    }

    private void publishSseEvent() {
        CrawlerEvent event = (new CrawlerEvent(crawlerEventId.incrementAndGet(), crawlerEventPayload.clone()));
        crawlerEventPublisher.publish(event);
    }

    private void publishCallbackEvent() {
        //  @formatter:off
        ResourceCallback resourceCallback =
                new ResourceCallback(task.getId(),
                                     stateCode.get(),
                                     statistic.getStartTime(),
                                     statistic.getEndTime(),
                                     statistic.getId());
        publisherFactory
                .getPublisher()
                .publishEvent(resourceCallback);
        //  @formatter:on
    }

    @Component
    @RequiredArgsConstructor
    public static final class Builder {

        private final StatisticService statisticService;
        private final PublisherFactory PublisherFactory;
        private final PageService pageService;

        public Crawler build() {
            return new Crawler(statisticService, PublisherFactory, pageService);
        }
    }
}
