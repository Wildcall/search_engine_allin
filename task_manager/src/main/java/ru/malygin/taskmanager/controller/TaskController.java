package ru.malygin.taskmanager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.malygin.taskmanager.facade.TaskFacade;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.TaskDto;
import ru.malygin.taskmanager.model.dto.view.TaskView;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskFacade taskFacade;

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<BaseDto> save(Authentication authentication,
                                        @Validated(TaskView.New.class) @RequestBody TaskDto taskDto) {
        BaseDto response = taskFacade.save(authentication, taskDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<BaseDto> update(Authentication authentication,
                                          @Validated(TaskView.Update.class) @RequestBody TaskDto taskDto) {
        BaseDto response = taskFacade.update(authentication, taskDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<List<BaseDto>> findAll(Authentication authentication) {
        List<BaseDto> response = taskFacade.findAll(authentication);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<BaseDto> findById(Authentication authentication,
                                            @PathVariable Long id) {
        BaseDto response = taskFacade.findById(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/type/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<List<BaseDto>> findAllByResourceStringType(Authentication authentication,
                                                                     @PathVariable String type) {
        List<BaseDto> response = taskFacade.findAllByResourceStringType(authentication, type);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/site/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<List<BaseDto>> findAllBySiteId(Authentication authentication,
                                                         @PathVariable Long id) {
        List<BaseDto> response = taskFacade.findAllBySiteId(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<List<BaseDto>> findAllByStatus(Authentication authentication,
                                                         @PathVariable String status) {
        List<BaseDto> response = taskFacade.findAllByTaskStateString(authentication, status);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> deleteById(Authentication authentication,
                                           @PathVariable Long id) {
        Long response = taskFacade.deleteById(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<BaseDto> start(Authentication authentication,
                                                   @PathVariable Long id) {
        BaseDto response = taskFacade.start(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<BaseDto> stop(Authentication authentication,
                                       @PathVariable Long id) {
        BaseDto response = taskFacade.stop(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
