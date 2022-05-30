package ru.malygin.searcher.searcher;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;
import ru.malygin.searcher.model.Task;
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
    private final Fetcher fetcher;
    private final StatisticService statisticService;
    private final PageService pageService;
    private final LemmaService lemmaService;
    private final IndexService indexService;
    // init
    private final AtomicInteger stateCode = new AtomicInteger(0);

    private final AtomicInteger fetchedPages = new AtomicInteger(0);
    private final AtomicInteger fetchedLemmas = new AtomicInteger(0);
    private final AtomicInteger fetchedIndex = new AtomicInteger(0);

    private final AtomicInteger savedPages = new AtomicInteger(0);
    private final AtomicInteger savedLemmas = new AtomicInteger(0);
    private final AtomicInteger savedIndex = new AtomicInteger(0);

    private final AtomicBoolean savedPagesDone = new AtomicBoolean(false);
    private final AtomicBoolean savedLemmasDone = new AtomicBoolean(false);
    private final AtomicBoolean savedIndexesDone = new AtomicBoolean(false);
    // init in start
    private Task task;
    private Map<Task, Searcher> currentRunningTasks;
    private Statistic statistic;
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
                      Map<Task, Searcher> currentRunningTasks) {
        //  @formatter:off
        this.task = task;
        this.siteId = task.getSiteId();
        this.appUserId = task.getAppUserId();
        this.currentRunningTasks = currentRunningTasks;

        this.statistic = new Statistic();
        this.statistic.setSiteId(siteId);
        this.statistic.setAppUserId(appUserId);

        Thread thread = new Thread(this);
        thread.setName("Searcher-" + task.getPath() + "-" + appUserId);
        thread.start();
    }

    public void stop() {
        stateCode.set(4);
    }

    @Override
    public void run() {
        statistic.setStartTime(LocalDateTime.now());

        stateCode.set(1);

        fetcher
                .fetchPages(siteId, appUserId)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(page -> {
                    if (stateCode.get() == 1 && page.hasRequiredField()) {
                        fetchedPages.incrementAndGet();
                        pageService
                                .save(page.withId(null))
                                .doOnSuccess(p -> savedPages.incrementAndGet())
                                .subscribe();
                    }
                })
                .doOnComplete(() -> savedPagesDone.set(true))
                .doOnError(throwable -> stateCode.set(5))
                .subscribe();

        fetcher
                .fetchLemmas(siteId, appUserId)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(lemma -> {
                    if (stateCode.get() == 1 && lemma.hasRequiredField())
                        fetchedLemmas.incrementAndGet();
                    lemmaService
                            .save(lemma.withId(null))
                            .doOnSuccess(l -> savedLemmas.incrementAndGet())
                            .subscribe();
                })
                .doOnComplete(() -> savedLemmasDone.set(true))
                .doOnError(throwable -> stateCode.set(5))
                .subscribe();

        fetcher
                .fetchIndexes(siteId, appUserId)
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(index -> {
                    if (stateCode.get() == 1 && index.hasRequiredField())
                        fetchedIndex.incrementAndGet();
                    indexService
                            .save(index.withId(null))
                            .doOnSuccess(i -> savedIndex.incrementAndGet())
                            .subscribe();
                })
                .doOnComplete(() -> savedIndexesDone.set(true))
                .doOnError(throwable -> stateCode.set(5))
                .subscribe();

        watchDogLoop();
    }

    private void watchDogLoop() {
        while (true) {
            timeOut100ms();
            // ERROR
            if (stateCode.get() == 5) {
                break;
            }
            // INTERRUPT
            if (stateCode.get() == 4) {
                saveFinalStat();
                break;
            }
            // COMPLETE
            if (savedPagesDone.get() && savedLemmasDone.get() && savedIndexesDone.get()) {
                if (savedPages.get() == fetchedPages.get() && savedLemmas.get() == fetchedLemmas.get() && savedIndex.get() == fetchedIndex.get()) {
                    stateCode.set(3);
                    saveFinalStat();
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
        statistic.setEndTime(stateCode.get() == 4 || stateCode.get() == 3 ? LocalDateTime.now() : null);
        statistic.setSavedPages(savedPages.get());
        statistic.setSavedLemmas(savedLemmas.get());
        statistic.setSavedIndexes(savedIndex.get());
    }

    @Component
    @RequiredArgsConstructor
    public static final class Builder {

        private final Fetcher fetcher;
        private final StatisticService statisticService;
        private final PageService pageService;
        private final LemmaService lemmaService;
        private final IndexService indexService;

        public Searcher build() {
            return new Searcher(fetcher, statisticService, pageService, lemmaService, indexService);
        }
    }
}
