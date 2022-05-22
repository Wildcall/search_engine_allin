package ru.malygin.searcher.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.impl.Index;

public interface IndexRepository extends ReactiveCrudRepository<Index, Long> {
    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);
}
