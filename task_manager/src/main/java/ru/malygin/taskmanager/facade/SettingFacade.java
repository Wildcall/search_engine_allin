package ru.malygin.taskmanager.facade;

import org.springframework.security.core.Authentication;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;

import java.util.List;

public interface SettingFacade {

    BaseDto save(Authentication authentication,
                 ResourceSettingDto resourceSettingDto);

    List<BaseDto> findAllByResourceStringType(Authentication authentication,
                                              String type);

    List<BaseDto> findAll(Authentication authentication);

    BaseDto findById(Authentication authentication,
                     Long id);

    Long deleteById(Authentication authentication,
                    Long id);

}
