package ru.malygin.searcher.service;

import reactor.core.publisher.Flux;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;

public interface SearcherService {
    Flux<SearchResponse> search(Long siteId,
                                String query);

    void start(Task task);

    void stop(Task task);
}
