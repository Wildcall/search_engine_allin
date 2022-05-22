package ru.malygin.indexer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.malygin.indexer.model.entity.impl.Index;
import ru.malygin.indexer.service.IndexService;

@Slf4j
@RestController
@RequestMapping("api/v1/index")
@RequiredArgsConstructor
public class IndexController {

    private final IndexService indexService;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Index> findPageBySiteIdAndAppUserId(@RequestParam Long siteId,
                                                    @RequestParam Long appUserId) {
        return indexService.findAllBySiteIdAndAppUserId(siteId, appUserId);
    }
}
