package ru.malygin.helper.service.senders.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.malygin.helper.model.SearchEngineServiceMetrics;
import ru.malygin.helper.service.senders.MetricsSender;

@Slf4j
@RequiredArgsConstructor
public class DefaultMetricsSender implements MetricsSender {
    @Override
    public void send(SearchEngineServiceMetrics metrics) {

    }
}
