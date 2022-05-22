package ru.malygin.crawler.crawler;

import lombok.Getter;
import ru.malygin.crawler.model.entity.impl.Page;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PageFetcher implements Runnable {

    private final Queue<String> links;
    private final Queue<Page> pages;
    private final SiteFetcher siteFetcher;
    @Getter
    private final AtomicBoolean serve = new AtomicBoolean(false);
    @Getter
    private final AtomicInteger completeTasks = new AtomicInteger(0);
    @Getter
    private Thread currentThread;
    private Boolean runFlag;

    public PageFetcher(Queue<String> links,
                       Queue<Page> pages,
                       SiteFetcher siteFetcher) {
        this.links = links;
        this.pages = pages;
        this.siteFetcher = siteFetcher;
    }

    public void start() {
        currentThread = new Thread(this);
        currentThread.setName("FetchPage-" + new Random().nextLong());
        currentThread.start();
    }

    public void stop() {
        runFlag = false;
    }

    @Override
    public void run() {
        try {
            runFlag = true;
            while (runFlag) {
                String link = links.poll();
                if (link != null) {
                    serve.set(true);
                    Page page = siteFetcher.fetchPath(link);
                    pages.add(page);
                    TimeUnit.MILLISECONDS.sleep(siteFetcher.getDelayTime());
                    this.completeTasks.incrementAndGet();
                    serve.set(false);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
