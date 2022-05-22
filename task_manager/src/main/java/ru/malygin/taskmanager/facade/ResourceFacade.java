package ru.malygin.taskmanager.facade;

import org.springframework.security.core.Authentication;
import ru.malygin.taskmanager.model.dto.BaseDto;

import java.util.Map;

public interface ResourceFacade {

    Map<String, Long> start(Authentication authentication,
              Long id);

    String stop(Authentication authentication,
                 Long id);
}
