package ru.malygin.crawler.crawler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.malygin.crawler.model.entity.impl.Page;
import ru.malygin.crawler.service.PageService;
import ru.malygin.crawler.sse.event.PageEvent;
import ru.malygin.crawler.sse.payload.PageEventPayload;
import ru.malygin.crawler.sse.publisher.PageEventPublisher;

import java.time.LocalDateTime;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PageSaver implements Runnable {

    private final PageService pageService;
    private final Queue<Page> pageToSave;
    private final Long siteId;
    private final Long appUserId;
    private final PageEventPublisher pageEventPublisher;
    @Getter
    private final AtomicBoolean serve = new AtomicBoolean(false);
    @Getter
    private final AtomicInteger completeTasks = new AtomicInteger(0);
    @Getter
    private final AtomicInteger errorsCount = new AtomicInteger(0);
    @Getter
    private Thread currentThread;
    private Boolean runFlag = true;

    public PageSaver(PageService pageService,
                     Queue<Page> pageToSave,
                     Long siteId,
                     Long appUserId,
                     PageEventPublisher pageEventPublisher) {
        this.pageService = pageService;
        this.pageToSave = pageToSave;
        this.siteId = siteId;
        this.appUserId = appUserId;
        this.pageEventPublisher = pageEventPublisher;
    }

    public void start() {
        currentThread = new Thread(this);
        currentThread.setName("SavePage-" + new Random().nextLong());
        currentThread.start();
    }

    public void stop() {
        runFlag = false;
    }

    @Override
    public void run() {
        while (runFlag) {
            Page page = pageToSave.poll();
            if (page != null) {
                serve.set(true);

                page.setSiteId(siteId);
                page.setAppUserId(appUserId);
                page.setCreateTime(LocalDateTime.now());

                pageService
                        .save(page)
                        .subscribe(p -> pageEventPublisher.publish(
                                new PageEvent(completeTasks.get(), PageEventPayload.fromPage(p))));

                if (page.getCode() != 200) errorsCount.incrementAndGet();
                completeTasks.incrementAndGet();
                serve.set(false);
            }
        }
    }
}
