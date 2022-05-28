package ru.malygin.crawler.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.crawler.model.entity.Statistic;

public interface StatisticRepository extends ReactiveCrudRepository<Statistic, Long> {

    Flux<Statistic> findAllBySiteId(Long siteId);

    Flux<Statistic> findAllByAppUserId(Long appUserId);

    Flux<Statistic> findAllBySiteIdAndAppUserId(Long siteId,
                                                Long appUserId);

    Mono<Void> deleteAllBySiteId(Long siteId);

    Mono<Void> deleteAllByAppUserId(Long appUserId);

    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);
}
