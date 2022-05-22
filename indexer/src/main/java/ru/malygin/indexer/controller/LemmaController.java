package ru.malygin.indexer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import ru.malygin.indexer.model.entity.impl.Lemma;
import ru.malygin.indexer.service.LemmaService;

@Slf4j
@RestController
@RequestMapping("api/v1/lemma")
@RequiredArgsConstructor
public class LemmaController {

    private final LemmaService lemmaService;

    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<Lemma> findPageBySiteIdAndAppUserId(@RequestParam Long siteId,
                                                    @RequestParam Long appUserId) {
        return lemmaService.findAllBySiteIdAndAppUserId(siteId, appUserId);
    }
}
