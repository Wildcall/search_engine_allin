package ru.malygin.indexer.service;

import reactor.core.publisher.Flux;
import ru.malygin.indexer.model.Page;
import ru.malygin.indexer.model.Task;

public interface PageResponseListener {
    Flux<Page> listenPageResponse(Task task,
                                  Long pageCount);
}
