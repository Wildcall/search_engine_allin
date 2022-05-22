package ru.malygin.taskmanager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.malygin.taskmanager.facade.SettingFacade;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.CrawlerSettingsDto;
import ru.malygin.taskmanager.model.dto.impl.IndexerSettingsDto;
import ru.malygin.taskmanager.model.dto.impl.SearcherSettingsDto;
import ru.malygin.taskmanager.model.dto.view.SettingView;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/setting")
public class SettingsController {

    private final SettingFacade settingFacade;

    @PostMapping(path = "/crawler", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SettingView.Response.class})
    public ResponseEntity<BaseDto> saveCrawlerSettings(Authentication authentication,
                                                       @Validated(SettingView.New.class) @RequestBody CrawlerSettingsDto crawlerSettingsDto) {
        BaseDto response = settingFacade
                .save(authentication, crawlerSettingsDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(path = "/indexer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SettingView.Response.class})
    public ResponseEntity<BaseDto> saveIndexerSettings(Authentication authentication,
                                                       @Validated(SettingView.New.class) @RequestBody IndexerSettingsDto indexerSettingsDto) {
        BaseDto response = settingFacade
                .save(authentication, indexerSettingsDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping(path = "/searcher", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SettingView.Response.class})
    public ResponseEntity<BaseDto> saveSearcherSettings(Authentication authentication,
                                                        @Validated(SettingView.New.class) @RequestBody SearcherSettingsDto searcherSettingsDto) {
        BaseDto response = settingFacade
                .save(authentication, searcherSettingsDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SettingView.Response.class})
    public ResponseEntity<List<BaseDto>> findAll(Authentication authentication) {
        List<BaseDto> response = settingFacade
                .findAll(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SettingView.Response.class})
    public ResponseEntity<BaseDto> findOne(Authentication authentication,
                                           @PathVariable Long id) {
        BaseDto response = settingFacade
                .findById(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({SettingView.Response.class})
    public ResponseEntity<List<BaseDto>> findAllByType(Authentication authentication,
                                                       @PathVariable String type) {
        List<BaseDto> response = settingFacade
                .findAllByResourceStringType(authentication, type);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> delete(Authentication authentication,
                                       @PathVariable Long id) {
        Long response = settingFacade.deleteById(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
