package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.Lemma;
import ru.malygin.searcher.repository.LemmaRepository;
import ru.malygin.searcher.service.LemmaService;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LemmaServiceImpl implements LemmaService {

    private final LemmaRepository lemmaRepository;

    @Override
    public Mono<Lemma> save(Lemma lemma) {
        return lemmaRepository.save(lemma);
    }

    @Override
    public Mono<Void> deleteAllBySiteIdAndAppUserId(Long siteId,
                                                    Long appUserId) {
        return lemmaRepository.deleteAllBySiteIdAndAppUserId(siteId, appUserId);
    }
}
