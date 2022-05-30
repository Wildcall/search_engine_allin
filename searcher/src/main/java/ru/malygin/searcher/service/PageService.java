package ru.malygin.searcher.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.entity.Page;

import java.util.List;

public interface PageService {
    Mono<Page> save(Page page);

    Flux<SearchResponse> search(Long siteId,
                                List<String> words);

    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);
}
