package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.entity.Page;
import ru.malygin.searcher.repository.PageRepository;
import ru.malygin.searcher.service.PageService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    @Override
    public Mono<Page> save(Page page) {
        return pageRepository.save(page);
    }

    @Override
    public Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                                    Long appUserId) {
        return pageRepository.deleteAllBySiteIdAndAppUserId(siteId, appUserId);
    }

    @Override
    public Flux<SearchResponse> search(Long siteId,
                                       List<String> words) {
        return pageRepository
                .search(words, siteId, words.size())
                .map(searchResult -> new SearchResponse(searchResult.path(), getTitle(searchResult.content())));
    }

    private String getTitle(String content) {
        return Jsoup
                .parse(content)
                .getElementsByTag("title")
                .text();
    }
}
