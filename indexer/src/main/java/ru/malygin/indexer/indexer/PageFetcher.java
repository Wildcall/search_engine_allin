package ru.malygin.indexer.indexer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.malygin.indexer.model.Page;

@Slf4j
@RequiredArgsConstructor
@Service
public class PageFetcher {

    public Flux<Page> fetch(Long siteId,
                            Long appUserId) {
        return Flux.empty();
    }
}
