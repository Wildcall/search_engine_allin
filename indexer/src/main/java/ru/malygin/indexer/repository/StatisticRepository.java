package ru.malygin.indexer.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.indexer.model.entity.impl.Statistic;

public interface StatisticRepository extends ReactiveCrudRepository<Statistic, Long> {
}
