package ru.malygin.searcher.service;

import reactor.core.publisher.Flux;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.model.TaskAction;

public interface SearcherService {
    Flux<SearchResponse> search(Long siteId,
                                String query);

    void process(Task task,
                 TaskAction action);
}
