package ru.malygin.crawler.sse.publisher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import ru.malygin.crawler.sse.event.PageEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Slf4j
@Getter
public class PageEventPublisher implements Consumer<FluxSink<PageEvent>> {

    private final Executor executor;
    private final Long siteId;
    private final Long appUserId;
    private final Flux<PageEvent> pageEventFlux;
    private final BlockingQueue<PageEvent> events = new LinkedBlockingQueue<>();

    public PageEventPublisher(Executor executor,
                              Long siteId,
                              Long appUserId) {
        this.executor = executor;
        this.siteId = siteId;
        this.appUserId = appUserId;
        this.pageEventFlux = Flux
                .create(this)
                .publish()
                .autoConnect();
        this.pageEventFlux.subscribe();
    }

    public void publish(PageEvent event) {
        events.offer(event);
    }

    @Override
    public void accept(FluxSink<PageEvent> pageEventFluxSink) {
        this.executor.execute(() -> {
            while (true) {
                try {
                    PageEvent event = events.take();
                    if (event.id() < 0) {
                        pageEventFluxSink.complete();
                        break;
                    }
                    pageEventFluxSink.next(event);
                } catch (InterruptedException e) {
                    log.info("IndexerEventPublisher-Oops...{}", e.getMessage());
                }
            }

        });
    }

    public void stop() {
        publish(new PageEvent(-1, null));
    }
}
