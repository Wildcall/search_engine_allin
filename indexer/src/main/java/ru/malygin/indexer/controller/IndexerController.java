package ru.malygin.indexer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import ru.malygin.indexer.model.Task;
import ru.malygin.indexer.model.dto.view.View;
import ru.malygin.indexer.service.IndexerService;

@Slf4j
@RestController
@RequestMapping("api/v1/indexer")
@RequiredArgsConstructor
public class IndexerController {

    private final IndexerService indexerService;

    @PostMapping(path = "start", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> start(@RequestBody @Validated(View.New.class) Task task) {
        return indexerService.start(task);
    }

    @PostMapping(path = "stop", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> stop(@RequestBody @Validated(View.New.class) Task task) {
        return indexerService.stop(task);
    }
}
