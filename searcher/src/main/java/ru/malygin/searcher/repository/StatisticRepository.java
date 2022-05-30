package ru.malygin.searcher.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.malygin.searcher.model.entity.Statistic;

public interface StatisticRepository extends ReactiveCrudRepository<Statistic, Long> {
}
