package ru.malygin.indexer.indexer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import ru.malygin.indexer.model.ResourceCallback;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.model.entity.impl.Statistic;
import ru.malygin.indexer.service.StatisticService;
import ru.malygin.indexer.sse.PublisherFactory;
import ru.malygin.indexer.sse.event.IndexerEvent;
import ru.malygin.indexer.sse.payload.IndexerEventPayload;
import ru.malygin.indexer.sse.publisher.IndexerEventPublisher;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Indexer implements Runnable {

    // init in builder
    private final StatisticService statisticService;
    private final PublisherFactory publisherFactory;
    private final PageParser.Builder builder;
    private final PageFetcher pageFetcher;
    // init
    private final AtomicInteger stateCode = new AtomicInteger(0);
    private final AtomicInteger parsedPages = new AtomicInteger(0);
    private final AtomicInteger completeRails = new AtomicInteger(0);
    private final AtomicInteger indexerEventId = new AtomicInteger(0);
    // components
    private PageParser pageParser;
    private IndexerEventPublisher indexerEventPublisher;
    // init in start
    private Task task;
    private Map<Task, Indexer> currentRunningTasks;
    private Statistic statistic;
    private IndexerEventPayload indexerEventPayload;
    private String sitePath;
    private Long siteId;
    private Long appUserId;
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
                      Map<Task, Indexer> currentRunningTasks) {
        //  @formatter:off
        this.task = task;
        this.siteId = task.getSiteId();
        this.appUserId = task.getAppUserId();
        this.sitePath = task.getPath();
        this.currentRunningTasks = currentRunningTasks;

        this.indexerEventPublisher = publisherFactory.createIndexerEventPublisher(siteId, appUserId);

        this.pageParser = builder
                .siteId(siteId)
                .appUserId(appUserId)
                .task(task)
                .build();

        this.statistic = new Statistic();
        this.statistic.setSiteId(siteId);
        this.statistic.setAppUserId(appUserId);

        this.indexerEventPayload = new IndexerEventPayload(task.getId(),
                                                           siteId,
                                                           appUserId,
                                                           stateCode.get(),
                                                           LocalDateTime.now(),
                                                           statistic);

        Thread thread = new Thread(this);
        thread.setName("Indexer-" + sitePath + "-" + appUserId);
        thread.start();
        //  @formatter:on
    }

    public void stop() {
        stateCode.set(4);
    }

    @Override
    public void run() {
        statistic.setStartTime(LocalDateTime.now());

        publishEvent();

        timerCounter = System.currentTimeMillis();

        stateCode.set(1);
        publishCallbackEvent();

        pageFetcher
                .fetch(siteId, appUserId)
                .parallel(task.getParallelism())
                .runOn(Schedulers.newParallel("parsePage-" + sitePath))
                .doOnNext(page -> {
                    if (stateCode.get() == 1 && page.notEmpty()) {
                        parsedPages.incrementAndGet();
                        pageParser.parsePage(page);
                    }
                })
                .doOnComplete(() -> {
                    if (completeRails.incrementAndGet() == task.getParallelism()) stateCode.set(2);
                })
                .doOnError(throwable -> stateCode.set(5))
                .subscribe();

        watchDogLoop();
    }

    private void watchDogLoop() {
        AtomicBoolean savedProcess = new AtomicBoolean(false);
        while (true) {
            timeOut100ms();
            // ERROR
            if (stateCode.get() == 5) {
                break;
            }
            // INTERRUPT
            if (stateCode.get() == 4) {
                saveAndPublishFinalStat();
                break;
            }
            // SAVE
            if (stateCode.get() == 2 && !savedProcess.get()) {
                pageParser
                        .saveLemmas()
                        .doOnSubscribe(subscription -> savedProcess.set(true))
                        .doOnComplete(() -> stateCode.set(3))
                        .subscribe();
            }
            // COMPLETE
            if (stateCode.get() == 3) {
                saveAndPublishFinalStat();
                break;
            }
            // SEND EVENT EVERY SendFreqInMs
            if (System.currentTimeMillis() - timerCounter > task.getEventFreqInMs()) {
                publishCurrentStat();
            }
        }
        currentRunningTasks.remove(task);
    }

    private void saveAndPublishFinalStat() {
        setActualStatistics();
        statisticService
                .save(statistic)
                .doOnSuccess(sink -> {
                    indexerEventPayload.setStatusCode(stateCode.get());
                    publishEvent();
                    publishCallbackEvent();
                    publisherFactory.deleteIndexerEventPublisher(indexerEventPublisher);
                })
                .subscribe();
    }

    private void publishCurrentStat() {
        indexerEventPayload.setStatusCode(stateCode.get());
        setActualStatistics();
        publishEvent();
        timerCounter = System.currentTimeMillis();
    }

    private void setActualStatistics() {
        statistic.setEndTime(stateCode.get() == 4 || stateCode.get() == 3 ? LocalDateTime.now() : null);
        statistic.setParsedPages(parsedPages.get());
        statistic.setSavedLemmas(pageParser
                                         .getCreatedLemmas()
                                         .get());
        statistic.setCreatedIndexes(pageParser
                                            .getSavedIndexes()
                                            .get());
    }

    private void publishEvent() {
        IndexerEvent event = (new IndexerEvent(indexerEventId.incrementAndGet(), indexerEventPayload.clone()));
        indexerEventPublisher.publish(event);
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
        private final PublisherFactory publisherFactory;
        private final PageParser.Builder builder;
        private final PageFetcher pageFetcher;

        public Indexer build() {
            return new Indexer(statisticService, publisherFactory, builder, pageFetcher);
        }
    }
}
