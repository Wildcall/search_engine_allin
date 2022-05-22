package ru.malygin.indexer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.indexer.model.entity.impl.Index;
import ru.malygin.indexer.repository.IndexRepository;
import ru.malygin.indexer.service.IndexService;

import java.util.List;

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
    public Flux<Index> findAllBySiteIdAndAppUserId(Long siteId,
                                                   Long appUserId) {
        return indexRepository.findAllBySiteIdAndAppUserId(siteId, appUserId);
    }

    @Override
    public Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                                    Long appUserId) {
        return indexRepository.deleteAllBySiteIdAndAppUserId(siteId, appUserId);
    }

    public Mono<Void> saveAll(List<Index> indexes) {
        return indexRepository
                .saveAll(indexes)
                .then();
    }
}
