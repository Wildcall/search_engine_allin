package ru.malygin.indexer.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.malygin.indexer.exception.ResponseException;
import ru.malygin.indexer.sse.event.IndexerEvent;
import ru.malygin.indexer.sse.publisher.IndexerEventPublisher;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class PublisherFactory {

    private final List<IndexerEventPublisher> indexerEventPublishers = new CopyOnWriteArrayList<>();
    private final Executor executor;
    private final ApplicationEventPublisher publisher;

    public Flux<IndexerEvent> getIndexerEventFluxFromPublisher(Long siteId,
                                                               Long appUserId) {
        IndexerEventPublisher indexerEventPublisher = indexerEventPublishers
                .stream()
                .filter(publisher -> publisher
                        .getSiteId()
                        .equals(siteId) && publisher
                        .getAppUserId()
                        .equals(appUserId))
                .findFirst()
                .orElse(null);

        if (indexerEventPublisher == null) return ResponseException.fluxResponseNoContent(
                "No active publisher");
        return indexerEventPublisher.getIndexerEventFlux();
    }

    public void deleteIndexerEventPublisher(IndexerEventPublisher indexerEventPublisher) {
        indexerEventPublishers.remove(indexerEventPublisher);
        indexerEventPublisher.stop();
    }

    public IndexerEventPublisher createIndexerEventPublisher(Long siteId,
                                                             Long appUserId) {
        IndexerEventPublisher indexerEventPublisher = new IndexerEventPublisher(executor, siteId,
                                                                                appUserId);
        indexerEventPublishers.add(indexerEventPublisher);
        return indexerEventPublisher;
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }
}
