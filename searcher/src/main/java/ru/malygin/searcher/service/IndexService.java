package ru.malygin.searcher.service;

import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.impl.Index;

public interface IndexService {

    Mono<Index> save(Index index);

    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);
}
