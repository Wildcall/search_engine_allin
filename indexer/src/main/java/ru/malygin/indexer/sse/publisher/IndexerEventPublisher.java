package ru.malygin.indexer.sse.publisher;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import ru.malygin.indexer.sse.event.IndexerEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

@Slf4j
@Getter
public class IndexerEventPublisher implements Consumer<FluxSink<IndexerEvent>> {

    private final Executor executor;
    private final Long siteId;
    private final Long appUserId;
    private final Flux<IndexerEvent> indexerEventFlux;
    private final BlockingQueue<IndexerEvent> events = new LinkedBlockingQueue<>();

    public IndexerEventPublisher(Executor executor,
                                 Long siteId,
                                 Long appUserId) {
        this.executor = executor;
        this.siteId = siteId;
        this.appUserId = appUserId;
        this.indexerEventFlux = Flux
                .create(this)
                .publish()
                .autoConnect();
        this.indexerEventFlux.subscribe();
    }

    public void publish(IndexerEvent event) {
        events.offer(event);
    }

    @Override
    public void accept(FluxSink<IndexerEvent> indexerEventFluxSink) {
        this.executor.execute(() -> {
            while (true) {
                try {
                    IndexerEvent event = events.take();
                    if (event.id() < 0) {
                        indexerEventFluxSink.complete();
                        break;
                    }
                    indexerEventFluxSink.next(event);
                } catch (InterruptedException e) {
                    log.info("IndexerEventPublisher-Oops...{}", e.getMessage());
                }
            }
        });
    }

    public void stop() {
        publish(new IndexerEvent(-1, null));
    }
}
