package ru.malygin.crawler.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.malygin.crawler.sse.event.CrawlerEvent;
import ru.malygin.crawler.sse.event.PageEvent;
import ru.malygin.crawler.sse.publisher.CrawlerEventPublisher;
import ru.malygin.crawler.sse.publisher.PageEventPublisher;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import static ru.malygin.crawler.exception.ResponseException.fluxResponseNoContent;

@Component
@RequiredArgsConstructor
public class PublisherFactory {

    private final List<CrawlerEventPublisher> crawlerEventPublishers = new CopyOnWriteArrayList<>();
    private final List<PageEventPublisher> pageEventPublishers = new CopyOnWriteArrayList<>();
    private final Executor executor;
    private final ApplicationEventPublisher publisher;

    public Flux<CrawlerEvent> getCrawlerEventFluxFromPublisher(Long siteId,
                                                               Long appUserId) {
        CrawlerEventPublisher crawlerEventPublisher = crawlerEventPublishers
                .stream()
                .filter(publisher -> publisher
                        .getSiteId()
                        .equals(siteId) && publisher
                        .getAppUserId()
                        .equals(appUserId))
                .findFirst()
                .orElse(null);

        if (crawlerEventPublisher == null) return fluxResponseNoContent("No active publisher");
        return crawlerEventPublisher.getCrawlerEventFlux();
    }

    public Flux<PageEvent> getPageEventFluxFromPublisher(Long siteId,
                                                         Long appUserId) {
        PageEventPublisher indexerEventPublisher = pageEventPublishers
                .stream()
                .filter(publisher -> publisher
                        .getSiteId()
                        .equals(siteId) && publisher
                        .getAppUserId()
                        .equals(appUserId))
                .findFirst()
                .orElse(null);

        if (indexerEventPublisher == null) return fluxResponseNoContent("No active publisher");
        return indexerEventPublisher.getPageEventFlux();
    }

    public void deleteCrawlerEventPublisher(CrawlerEventPublisher crawlerEventPublisher) {
        crawlerEventPublishers.remove(crawlerEventPublisher);
        crawlerEventPublisher.stop();
    }

    public void deletePageEventPublisher(PageEventPublisher pageEventPublisher) {
        pageEventPublishers.remove(pageEventPublisher);
        pageEventPublisher.stop();
    }

    public CrawlerEventPublisher createCrawlerEventPublishers(Long siteId,
                                                              Long appUserId) {
        CrawlerEventPublisher crawlerEventPublisher = new CrawlerEventPublisher(executor, siteId,
                                                                                appUserId);
        crawlerEventPublishers.add(crawlerEventPublisher);
        return crawlerEventPublisher;
    }

    public PageEventPublisher createPageEventPublisher(Long siteId,
                                                       Long appUserId) {
        PageEventPublisher pageEventPublisher = new PageEventPublisher(executor, siteId, appUserId);
        pageEventPublishers.add(pageEventPublisher);
        return pageEventPublisher;
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }
}
