package ru.malygin.searcher.searcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.searcher.model.entity.Index;
import ru.malygin.searcher.model.entity.Lemma;
import ru.malygin.searcher.model.entity.Page;

@Slf4j
@RequiredArgsConstructor
@Service
public class Fetcher {

    public Flux<Page> fetchPages(Long siteId,
                                 Long appUserId) {
        return Flux.empty();
    }

    public Flux<Lemma> fetchLemmas(Long siteId,
                                   Long appUserId) {
        return Flux.empty();
    }

    public Flux<Index> fetchIndexes(Long siteId,
                                    Long appUserId) {
        return Flux.empty();
    }
}
