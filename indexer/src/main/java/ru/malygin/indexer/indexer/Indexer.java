package ru.malygin.indexer.indexer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.model.TaskCallbackEvent;
import ru.malygin.helper.model.TaskState;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.model.entity.Statistic;
import ru.malygin.indexer.service.StatisticService;

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
    private final PageParser.Builder builder;
    private final PageFetcher pageFetcher;
    private final ApplicationEventPublisher publisher;
    // init
    private final AtomicInteger stateCode = new AtomicInteger(0);
    private final AtomicInteger parsedPages = new AtomicInteger(0);
    private final AtomicInteger completeRails = new AtomicInteger(0);
    // components
    private PageParser pageParser;
    // init in start
    private TaskState taskState = TaskState.CREATE;
    private Task task;
    private Map<Task, Indexer> currentRunningTasks;
    private Statistic statistic;
    private String sitePath;
    private Long siteId;
    private Long appUserId;


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


        this.pageParser = builder
                .siteId(siteId)
                .appUserId(appUserId)
                .task(task)
                .build();

        this.statistic = new Statistic();
        this.statistic.setSiteId(siteId);
        this.statistic.setAppUserId(appUserId);

        Thread thread = new Thread(this);
        thread.setName("Indexer-" + sitePath + "-" + appUserId);
        thread.start();
        //  @formatter:on
    }

    /**
     * The method interrupts the algorithm
     */
    public void stop() {
        changeTaskState(TaskState.INTERRUPT);
    }

    @Override
    public void run() {
        statistic.setStartTime(LocalDateTime.now());

        changeTaskState(TaskState.START);

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
        }
        currentRunningTasks.remove(task);
    }

    private void saveAndPublishFinalStat() {
        setActualStatistics();
        statisticService
                .save(statistic)
                .subscribe();
    }

    private void setActualStatistics() {
        statistic.setEndTime(!taskState.equals(TaskState.START) ? LocalDateTime.now() : null);
        statistic.setParsedPages(parsedPages.get());
        statistic.setSavedLemmas(pageParser
                                         .getCreatedLemmas()
                                         .get());
        statistic.setCreatedIndexes(pageParser
                                            .getSavedIndexes()
                                            .get());
    }

    private void changeTaskState(TaskState state) {
        this.taskState = state;
        setActualStatistics();
        publisher.publishEvent(new TaskCallbackEvent(
                new TaskCallback(task.getId(), taskState, statistic.getStartTime(), statistic.getEndTime())));
    }

    @Component
    @RequiredArgsConstructor
    public static final class Builder {

        private final StatisticService statisticService;
        private final PageParser.Builder builder;
        private final PageFetcher pageFetcher;
        private final ApplicationEventPublisher publisher;

        public Indexer build() {
            return new Indexer(statisticService, builder, pageFetcher, publisher);
        }
    }
}
