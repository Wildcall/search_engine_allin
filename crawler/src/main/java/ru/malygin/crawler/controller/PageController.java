package ru.malygin.crawler.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.malygin.crawler.model.entity.impl.Page;
import ru.malygin.crawler.service.PageService;

@Slf4j
@RestController
@RequestMapping("api/v1/page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Page> findPageBySiteIdAndAppUserId(@RequestParam Long siteId,
                                                   @RequestParam Long appUserId) {
        return pageService.findAllBySiteIdAndAppUserId(siteId, appUserId);
    }
}
