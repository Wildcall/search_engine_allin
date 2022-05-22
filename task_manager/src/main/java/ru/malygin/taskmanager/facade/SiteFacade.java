package ru.malygin.taskmanager.facade;

import org.springframework.security.core.Authentication;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.SiteDto;

import java.util.List;

public interface SiteFacade {

    BaseDto save(Authentication authentication,
                 SiteDto siteDto);

    List<BaseDto> findAll(Authentication authentication);

    BaseDto findById(Authentication authentication,
                     Long id);

    Long deleteById(Authentication authentication,
                    Long id);
}
