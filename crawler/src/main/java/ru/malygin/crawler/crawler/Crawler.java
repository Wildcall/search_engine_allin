package ru.malygin.crawler.crawler;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.model.entity.Page;
import ru.malygin.crawler.model.entity.Statistic;
import ru.malygin.crawler.service.PageService;
import ru.malygin.crawler.service.StatisticService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The class that performs the distribution of tasks for crawling and downloading sites, parsing content and saving the site content to the database.
 * A class object can be created using {@link Builder}
 *
 * @author Nikolay Malygin
 * @version 1.0
 * @see PageFetcher
 * @see LinkParser
 * @see PageSaver
 */

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Crawler implements Runnable {

    // init in builder
    private final StatisticService statisticService;
    private final PageService pageService;
    // init
    private final AtomicInteger stateCode = new AtomicInteger(0);
    private final Queue<Page> pagesQueue = new ConcurrentLinkedQueue<>();
    private final Queue<String> linksQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Page> saveQueue = new ConcurrentLinkedQueue<>();
    // components
    private PageFetcher pageFetcher;
    private PageSaver pageSaver;
    private LinkParser linkParser;
    // init in start
    private Task task;
    private Map<Task, Crawler> currentRunningTasks;
    private Statistic statistic;
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

    /**
     * The method initializes all child services, and starts the algorithm to scan site, the parsing and saving site
     *
     * @param task                the task for this service {@link Task}
     * @param currentRunningTasks the map of all tasks for this service
     * @see PageFetcher
     * @see LinkParser
     * @see PageSaver
     */
    public void start(Task task,
                      Map<Task, Crawler> currentRunningTasks) {
        //  @formatter:off
        this.task = task;
        Long siteId = task.getSiteId();
        Long appUserId = task.getAppUserId();
        this.sitePath = task.getPath();
        this.currentRunningTasks = currentRunningTasks;

        this.pageFetcher = new PageFetcher(linksQueue,
                                           pagesQueue,
                                           SiteFetcher.getFromTask(task));

        this.linkParser = new LinkParser(linksQueue,
                                         pagesQueue,
                                         saveQueue);

        this.pageSaver = new PageSaver(pageService,
                                       saveQueue,
                                       siteId,
                                       appUserId);

        this.statistic = new Statistic();
        this.statistic.setSiteId(siteId);
        this.statistic.setAppUserId(appUserId);

        Thread thread = new Thread(this);
        thread.setName("Crawler-" + sitePath + "-" + appUserId);
        thread.start();
        //  @formatter:on
    }

    /**
     * The method interrupts the algorithm
     */
    public void stop() {
        stateCode.set(4);
    }

    @Override
    public void run() {
        statistic.setStartTime(LocalDateTime.now());
        timerCounter = System.currentTimeMillis();

        stateCode.set(1);

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
        //  @formatter:off
        if (pagesQueue.isEmpty()
                && linksQueue.isEmpty()
                && saveQueue.isEmpty()
                && !pageFetcher.getServe()
                && !linkParser.getServe()
                && !pageSaver.getServe()) {
            stateCode.set(3);
        }
        //  @formatter:on
    }

    private void saveAndPublishFinalStat() {
        setActualStatistics();
        statisticService
                .save(statistic)
                .subscribe();

    }

    private void publishCurrentStat() {
        setActualStatistics();
        timerCounter = System.currentTimeMillis();
    }

    private void setActualStatistics() {
        statistic.setEndTime(stateCode.get() == 4 || stateCode.get() == 3 ? LocalDateTime.now() : null);
        statistic.setSavedPages(pageSaver.getCompleteTasks());
        statistic.setFetchPages(pageFetcher.getCompleteTasks());
        statistic.setErrors(pageSaver.getErrorsCount());
        statistic.setLinksCount(linkParser.getCompleteTasks());
    }

    @Component
    @RequiredArgsConstructor
    public static final class Builder {

        private final StatisticService statisticService;
        private final PageService pageService;

        public Crawler build() {
            return new Crawler(statisticService, pageService);
        }
    }
}
