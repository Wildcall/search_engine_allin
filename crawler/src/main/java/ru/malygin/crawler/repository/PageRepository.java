package ru.malygin.crawler.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.crawler.model.entity.impl.Page;

public interface PageRepository extends ReactiveCrudRepository<Page, Long> {

    Flux<Page> findAllBySiteId(Long siteId);

    Flux<Page> findAllByAppUserId(Long appUserId);

    Flux<Page> findAllBySiteIdAndAppUserId(Long siteId,
                                           Long appUserId);


    Mono<Void> deleteAllBySiteId(Long siteId);

    Mono<Void> deleteAllByAppUserId(Long appUserId);

    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);

    Mono<Long> countPagesBySiteIdAndAppUserId(Long siteId,
                                              Long appUserId);
}
