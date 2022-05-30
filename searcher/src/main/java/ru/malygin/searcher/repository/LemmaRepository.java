package ru.malygin.searcher.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.Lemma;

public interface LemmaRepository extends ReactiveCrudRepository<Lemma, Long> {
    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);
}
