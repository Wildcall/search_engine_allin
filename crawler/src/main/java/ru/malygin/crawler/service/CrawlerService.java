package ru.malygin.crawler.service;

import reactor.core.publisher.Mono;
import ru.malygin.crawler.model.Task;

public interface CrawlerService {
    Mono<String> start(Task task);

    Mono<String> stop(Task task);
}
