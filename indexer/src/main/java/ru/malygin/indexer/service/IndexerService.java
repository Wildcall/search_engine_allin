package ru.malygin.indexer.service;

import reactor.core.publisher.Mono;
import ru.malygin.indexer.model.Task;

public interface IndexerService {
    Mono<String> start(Task task);

    Mono<String> stop(Task task);
}
