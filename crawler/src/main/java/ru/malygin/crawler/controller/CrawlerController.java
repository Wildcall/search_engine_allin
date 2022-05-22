package ru.malygin.crawler.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.malygin.crawler.model.Task;
import ru.malygin.crawler.model.dto.view.View;
import ru.malygin.crawler.service.CrawlerService;

@Slf4j
@RestController
@RequestMapping("api/v1/crawler")
@RequiredArgsConstructor
public class CrawlerController {

    private final CrawlerService crawlerService;

    @PostMapping(path = "start", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> start(@RequestBody @Validated(View.New.class) Task task) {
        return crawlerService.start(task);
    }

    @PostMapping(path = "stop", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> stop(@RequestBody @Validated(View.New.class) Task task) {
        return crawlerService.stop(task);
    }
}
