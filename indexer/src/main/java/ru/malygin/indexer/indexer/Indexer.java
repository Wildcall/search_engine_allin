package ru.malygin.indexer.indexer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.helper.events.TaskCallbackEvent;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.model.entity.Statistic;
import ru.malygin.indexer.service.PageResponseListener;
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
    private final PageResponseListener pageResponseListener;
    private final ApplicationEventPublisher publisher;
    // init
    private final AtomicInteger parsedPages = new AtomicInteger(0);
    private final AtomicInteger completeRails = new AtomicInteger(0);
    private final AtomicBoolean saving = new AtomicBoolean(false);
    // components
    private PageParser pageParser;
    // init in start
    private TaskState taskState = TaskState.CREATE;
    private Task task;
    private Map<Task, Indexer> currentRunningTasks;
    private Statistic statistic;
    private String sitePath;
    private Long pageCount;


    private static void timeOut100ms() {
        try {
            TimeUnit.MILLISECONDS.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(Task task,
                      Long pageCount,
                      Map<Task, Indexer> currentRunningTasks) {
        //  @formatter:off
        this.task = task;
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();
        this.sitePath = task.getPath();
        this.currentRunningTasks = currentRunningTasks;
        this.pageCount = pageCount;

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

        pageResponseListener
                .listenPageResponse(task, pageCount)
                .parallel(task.getParallelism())
                .runOn(Schedulers.newParallel("parsePage-" + sitePath))
                .doOnNext(page -> {
                    if (taskState.equals(TaskState.START)) {
                        parsedPages.incrementAndGet();
                        pageParser.parsePage(page);
                    }
                })
                .doOnComplete(() -> {
                    if (completeRails.incrementAndGet() == task.getParallelism()) saving.set(true);
                })
                .doOnError(throwable -> changeTaskState(TaskState.ERROR))
                .subscribe();

        watchDogLoop();
    }

    private void watchDogLoop() {
        AtomicBoolean savedProcess = new AtomicBoolean(false);
        while (true) {
            timeOut100ms();

            if (taskState.equals(TaskState.ERROR)) break;

            // INTERRUPT
            if (taskState.equals(TaskState.INTERRUPT)) {
                saveAndPublishFinalStat();
                break;
            }
            // SAVE
            if (saving.get() && !savedProcess.get()) {
                pageParser
                        .saveLemmas()
                        .doOnSubscribe(subscription -> savedProcess.set(true))
                        .doOnComplete(() -> changeTaskState(TaskState.COMPLETE))
                        .subscribe();
            }
            // COMPLETE
            if (taskState.equals(TaskState.COMPLETE)) {
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
        private final PageResponseListener pageResponseListener;
        private final ApplicationEventPublisher publisher;

        public Indexer build() {
            return new Indexer(statisticService, builder, pageResponseListener, publisher);
        }
    }
}
