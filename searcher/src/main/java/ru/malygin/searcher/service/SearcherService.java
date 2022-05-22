package ru.malygin.searcher.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;

public interface SearcherService {
    Mono<String> start(Task task);

    Mono<String> stop(Task task);

    Flux<SearchResponse> search(Long siteId,
                                String query);
}
