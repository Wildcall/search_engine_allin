package ru.malygin.crawler.crawler;

import lombok.Getter;
import ru.malygin.crawler.model.entity.impl.Page;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class LinkParser implements Runnable {

    private final Queue<String> links;
    private final Queue<Page> pages;
    private final Queue<Page> pageToSave;
    @Getter
    private final AtomicBoolean serve = new AtomicBoolean(false);
    @Getter
    private final AtomicInteger completeTasks = new AtomicInteger(0);
    @Getter
    private Thread currentThread;
    private Boolean runFlag;

    public LinkParser(Queue<String> links,
                      Queue<Page> pages,
                      Queue<Page> pageToSave) {
        this.links = links;
        this.pages = pages;
        this.pageToSave = pageToSave;
    }

    public void start() {
        currentThread = new Thread(this);
        currentThread.setName("ParseLinks-" + new Random().nextLong());
        currentThread.start();
    }

    public void stop() {
        runFlag = false;
    }

    @Override
    public void run() {
        runFlag = true;
        while (runFlag) {
            Page page = pages.poll();
            if (page != null) {
                serve.set(true);
                pageToSave.add(page);
                if (page.getCode() == 200) links.addAll(SiteFetcher.getLinks(page));
                this.completeTasks.incrementAndGet();
                serve.set(false);
            }

        }
    }
}
