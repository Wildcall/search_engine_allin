package ru.malygin.taskmanager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.malygin.taskmanager.facade.SiteFacade;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.SiteDto;
import ru.malygin.taskmanager.model.dto.view.SiteView;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/site")
public class SiteController {

    private final SiteFacade siteFacade;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SiteView.Response.class})
    public ResponseEntity<BaseDto> save(Authentication authentication,
                                        @Validated(SiteView.New.class) @RequestBody SiteDto siteDto) {
        BaseDto response = siteFacade
                .save(authentication, siteDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SiteView.Response.class})
    public ResponseEntity<List<BaseDto>> findAll(Authentication authentication) {
        List<BaseDto> response = siteFacade
                .findAll(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SiteView.Response.class})
    public ResponseEntity<BaseDto> findById(Authentication authentication,
                                            @PathVariable Long id) {
        BaseDto response = siteFacade
                .findById(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> delete(Authentication authentication,
                                       @PathVariable Long id) {
        Long response = siteFacade.deleteById(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
