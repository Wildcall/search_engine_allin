package ru.malygin.searcher.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.entity.Statistic;
import ru.malygin.searcher.repository.StatisticRepository;
import ru.malygin.searcher.service.StatisticService;

@RequiredArgsConstructor
@Transactional
@Service
public class StatisticsServiceImpl implements StatisticService {

    private final StatisticRepository statisticRepository;

    @Override
    public Mono<Statistic> save(Statistic statistic) {
        return statisticRepository.save(statistic);
    }
}
