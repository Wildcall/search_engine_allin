package ru.malygin.crawler.controller;

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
import ru.malygin.crawler.sse.PublisherFactory;

/**
 * @author Nikolay Malygin
 * @version 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/sse")
@RestController
public class SSEController {

    private final ObjectMapper objectMapper;
    private final PublisherFactory PublisherFactory;

    @GetMapping(path = "crawler", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> crawlerEventsStream(@RequestParam Long siteId,
                                            @RequestParam Long appUserId) {
        return PublisherFactory
                .getCrawlerEventFluxFromPublisher(siteId, appUserId)
                .map(pce -> {
                    try {
                        return objectMapper.writeValueAsString(pce);
                    } catch (JsonProcessingException e) {
                        log.error("Error logging: {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
    }

    @GetMapping(path = "page", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> pageEventsStream(@RequestParam Long siteId,
                                         @RequestParam Long appUserId) {
        return PublisherFactory
                .getPageEventFluxFromPublisher(siteId, appUserId)
                .map(pce -> {
                    try {
                        return objectMapper.writeValueAsString(pce);
                    } catch (JsonProcessingException e) {
                        log.error("Error logging: {}", e.getMessage());
                        throw new RuntimeException(e);
                    }
                });
    }
}