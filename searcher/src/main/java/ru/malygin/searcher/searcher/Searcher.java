package ru.malygin.searcher.searcher;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.helper.model.TaskCallback;
import ru.malygin.helper.model.requests.DataRequest;
import ru.malygin.helper.senders.TaskCallbackSender;
import ru.malygin.helper.service.DataReceiver;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.model.entity.Index;
import ru.malygin.searcher.model.entity.Lemma;
import ru.malygin.searcher.model.entity.Page;
import ru.malygin.searcher.model.entity.Statistic;
import ru.malygin.searcher.service.IndexService;
import ru.malygin.searcher.service.LemmaService;
import ru.malygin.searcher.service.PageService;
import ru.malygin.searcher.service.StatisticService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Searcher implements Runnable {

    // init in builder
    private final StatisticService statisticService;
    private final PageService pageService;
    private final LemmaService lemmaService;
    private final IndexService indexService;
    private final DataReceiver dataReceiver;
    private final TaskCallbackSender taskCallbackSender;
    // init
    private final AtomicInteger savedPages = new AtomicInteger(0);
    private final AtomicInteger savedLemmas = new AtomicInteger(0);
    private final AtomicInteger savedIndex = new AtomicInteger(0);

    private final AtomicBoolean savedPagesDone = new AtomicBoolean(false);
    private final AtomicBoolean savedLemmasDone = new AtomicBoolean(false);
    private final AtomicBoolean savedIndexesDone = new AtomicBoolean(false);
    // init in start
    private TaskState taskState = TaskState.CREATE;
    private Task task;
    private Map<Task, Searcher> currentRunningTasks;
    private Statistic statistic;
    private DataRequest pageDataRequest;
    private DataRequest lemmaDataRequest;
    private DataRequest indexDataRequest;

    private static void timeOut100ms() {
        try {
            TimeUnit.MILLISECONDS.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void start(Task task,
                      DataRequest pageDataRequest,
                      DataRequest lemmaDataRequest,
                      DataRequest indexDataRequest,
                      Map<Task, Searcher> currentRunningTasks) {
        //  @formatter:off
        this.task = task;
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();
        this.currentRunningTasks = currentRunningTasks;
        this.pageDataRequest = pageDataRequest;
        this.lemmaDataRequest = lemmaDataRequest;
        this.indexDataRequest = indexDataRequest;

        this.statistic = new Statistic();
        this.statistic.setSiteId(siteId);
        this.statistic.setAppUserId(appUserId);

        Thread thread = new Thread(this);
        thread.setName("Searcher-" + task.getPath() + "-" + appUserId);
        thread.start();
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

        dataReceiver
                .receiveData(pageDataRequest, Page.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(page -> pageService
                        .save(page.withId(null))
                        .doOnSuccess(p -> savedPages.incrementAndGet())
                        .subscribe())
                .doOnComplete(() -> savedPagesDone.set(true))
                .doOnError(throwable -> changeTaskState(TaskState.ERROR))
                .subscribe();

        dataReceiver
                .receiveData(lemmaDataRequest, Lemma.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(lemma -> lemmaService
                        .save(lemma.withId(null))
                        .doOnSuccess(l -> savedLemmas.incrementAndGet())
                        .subscribe())
                .doOnComplete(() -> savedLemmasDone.set(true))
                .doOnError(throwable -> changeTaskState(TaskState.ERROR))
                .subscribe();

        dataReceiver
                .receiveData(indexDataRequest, Index.class)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(index -> indexService
                        .save(index.withId(null))
                        .doOnSuccess(i -> savedIndex.incrementAndGet())
                        .subscribe())
                .doOnComplete(() -> savedIndexesDone.set(true))
                .doOnError(throwable -> changeTaskState(TaskState.ERROR))
                .subscribe();

        watchDogLoop();
    }

    private void watchDogLoop() {
        while (true) {
            timeOut100ms();

            if (taskState.equals(TaskState.ERROR)) break;

            if (taskState.equals(TaskState.INTERRUPT)) {
                saveFinalStat();
                break;
            }

            if (savedPagesDone.get() && savedLemmasDone.get() && savedIndexesDone.get()) {
                if (savedPages.get() == pageDataRequest.getDataCount()
                        && savedLemmas.get() == lemmaDataRequest.getDataCount()
                        && savedIndex.get() == indexDataRequest.getDataCount()) {
                    saveFinalStat();
                    changeTaskState(TaskState.COMPLETE);
                    break;
                }
            }
        }
        currentRunningTasks.remove(task);
    }

    private void saveFinalStat() {
        setActualStatistics();
        statisticService
                .save(statistic)
                .subscribe();
    }

    private void setActualStatistics() {
        statistic.setEndTime(!taskState.equals(TaskState.START) ? LocalDateTime.now() : null);
        statistic.setSavedPages(savedPages.get());
        statistic.setSavedLemmas(savedLemmas.get());
        statistic.setSavedIndexes(savedIndex.get());
    }

    private void changeTaskState(TaskState state) {
        this.taskState = state;
        setActualStatistics();
        TaskCallback callback = new TaskCallback(task.getId(), taskState, statistic.getStartTime(), statistic.getEndTime());
        taskCallbackSender.send(callback);
    }

    @Component
    @RequiredArgsConstructor
    public static final class Builder {

        private final StatisticService statisticService;
        private final PageService pageService;
        private final LemmaService lemmaService;
        private final IndexService indexService;
        private final DataReceiver dataReceiver;
        private final TaskCallbackSender taskCallbackSender;

        public Searcher build() {
            return new Searcher(statisticService, pageService, lemmaService, indexService, dataReceiver, taskCallbackSender);
        }
    }
}
