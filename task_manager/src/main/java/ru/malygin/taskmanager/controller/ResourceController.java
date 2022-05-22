package ru.malygin.taskmanager.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.malygin.taskmanager.facade.ResourceFacade;
import ru.malygin.taskmanager.model.dto.view.TaskView;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/resource")
public class ResourceController {

    private final ResourceFacade resourceFacade;

    @GetMapping(path = "/start/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<Map<String, Long>> start(Authentication authentication,
                                                   @PathVariable Long id) {
        Map<String, Long> response = resourceFacade.start(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping(path = "/stop/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView({TaskView.Response.class})
    public ResponseEntity<String> stop(Authentication authentication,
                                       @PathVariable Long id) {
        String response = resourceFacade.stop(authentication, id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
