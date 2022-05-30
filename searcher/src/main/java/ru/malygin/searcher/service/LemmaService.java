package ru.malygin.searcher.service;

import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.Lemma;

public interface LemmaService {

    Mono<Lemma> save(Lemma lemma);

    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);
}
