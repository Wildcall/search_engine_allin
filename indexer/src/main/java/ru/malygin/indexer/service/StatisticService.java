package ru.malygin.indexer.service;

import reactor.core.publisher.Mono;
import ru.malygin.indexer.model.entity.impl.Statistic;

public interface StatisticService {
    Mono<Statistic> save(Statistic statistic);
}
