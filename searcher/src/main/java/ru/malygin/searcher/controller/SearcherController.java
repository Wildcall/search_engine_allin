package ru.malygin.searcher.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.malygin.searcher.model.SearchResponse;
import ru.malygin.searcher.model.Task;
import ru.malygin.searcher.model.dto.view.View;
import ru.malygin.searcher.service.SearcherService;

@Slf4j
@RestController
@RequestMapping("api/v1/searcher")
@RequiredArgsConstructor
public class SearcherController {

    private final SearcherService searcherService;

    @PostMapping(path = "start", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> start(@RequestBody @Validated(View.New.class) Task task) {
        return searcherService.start(task);
    }

    @PostMapping(path = "stop", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> stop(@RequestBody @Validated(View.New.class) Task task) {
        return searcherService.stop(task);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<SearchResponse> search(@RequestParam Long siteId,
                                       @RequestParam String query) {
        return searcherService.search(siteId, query);
    }
}
