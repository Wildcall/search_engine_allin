package ru.malygin.searcher.service;

import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.impl.Statistic;

public interface StatisticService {
    Mono<Statistic> save(Statistic statistic);
}
