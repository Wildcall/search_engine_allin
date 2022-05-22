package ru.malygin.indexer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.malygin.indexer.sse.PublisherFactory;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/sse")
@RestController
public class SSEController {

    private final ObjectMapper objectMapper;
    private final PublisherFactory PublisherFactory;

    @GetMapping(path = "indexer", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> indexerEventsStream(@RequestParam Long siteId,
                                            @RequestParam Long appUserId) {
        return PublisherFactory
                .getIndexerEventFluxFromPublisher(siteId, appUserId)
                .map(pce -> {
                    try {
                        return objectMapper.writeValueAsString(pce);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
