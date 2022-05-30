package ru.malygin.searcher.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.SearchResult;
import ru.malygin.searcher.model.entity.Page;

import java.util.List;

public interface PageRepository extends ReactiveCrudRepository<Page, Long> {

    Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                             Long appUserId);

    @Query("select _page.path, _page.content " +
            "from _page " +
            "inner join (select pagePath, lemmaRank " +
            "from (select _index.page_path as pagePath, " +
            "count(_index.page_path) as pageCount, " +
            "sum(rank) as lemmaRank " +
            "from _index " +
            "where _index.word in (:words) and _index.site_id = (:siteId) " +
            "group by _index.page_path) " +
            "as tmp " +
            "where pageCount = (:lemmasCount) " +
            "order by lemmaRank desc) " +
            "as tmp on _page.path = tmp.pagePath")
    Flux<SearchResult> search(List<String> words,
                              Long siteId,
                              Integer lemmasCount);
}