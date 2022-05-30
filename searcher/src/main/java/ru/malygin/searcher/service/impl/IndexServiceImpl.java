package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.Index;
import ru.malygin.searcher.repository.IndexRepository;
import ru.malygin.searcher.service.IndexService;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class IndexServiceImpl implements IndexService {

    private final IndexRepository indexRepository;

    @Override
    public Mono<Index> save(Index index) {
        return indexRepository.save(index);
    }

    @Override
    public Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                                    Long appUserId) {
        return indexRepository.deleteAllBySiteIdAndAppUserId(siteId, appUserId);
    }
}
