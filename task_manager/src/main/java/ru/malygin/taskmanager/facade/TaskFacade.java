package ru.malygin.taskmanager.facade;

import org.springframework.security.core.Authentication;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.TaskDto;

import java.util.List;

public interface TaskFacade {

    BaseDto save(Authentication authentication,
                 TaskDto taskDto);

    BaseDto update(Authentication authentication,
                   TaskDto taskDto);

    BaseDto findById(Authentication authentication,
                     Long id);

    List<BaseDto> findAll(Authentication authentication);

    List<BaseDto> findAllBySiteId(Authentication authentication,
                                  Long siteId);

    List<BaseDto> findAllByResourceStringType(Authentication authentication,
                                              String type);

    List<BaseDto> findAllByTaskStateString(Authentication authentication,
                                           String status);

    Long deleteById(Authentication authentication,
                    Long id);

    BaseDto start(Authentication authentication,
                  Long id);

    BaseDto stop(Authentication authentication,
                 Long id);
}
