package ru.malygin.crawler.sse.publisher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import ru.malygin.crawler.sse.event.CrawlerEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Slf4j
@Getter
public class CrawlerEventPublisher implements Consumer<FluxSink<CrawlerEvent>> {

    private final Executor executor;
    private final Long siteId;
    private final Long appUserId;
    private final Flux<CrawlerEvent> crawlerEventFlux;
    private final BlockingQueue<CrawlerEvent> events = new LinkedBlockingQueue<>();

    public CrawlerEventPublisher(Executor executor,
                                 Long siteId,
                                 Long appUserId) {
        this.executor = executor;
        this.siteId = siteId;
        this.appUserId = appUserId;
        this.crawlerEventFlux = Flux
                .create(this)
                .publish()
                .autoConnect();
        this.crawlerEventFlux.subscribe();
    }

    public void publish(CrawlerEvent event) {
        events.offer(event);
    }

    @Override
    public void accept(FluxSink<CrawlerEvent> crawlerEventFluxSink) {
        this.executor.execute(() -> {
            while (true) {
                try {
                    CrawlerEvent event = events.take();
                    if (event.id() < 0) {
                        crawlerEventFluxSink.complete();
                        break;
                    }
                    crawlerEventFluxSink.next(event);
                } catch (InterruptedException e) {
                    log.info("CrawlerEventPublisher-Oops...{}", e.getMessage());
                }
            }
        });
    }

    public void stop() {
        publish(new CrawlerEvent(-1, null));
    }
}
