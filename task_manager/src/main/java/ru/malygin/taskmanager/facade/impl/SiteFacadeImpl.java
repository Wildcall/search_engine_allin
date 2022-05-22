package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.facade.SiteFacade;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.SiteDto;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.service.AppUserService;
import ru.malygin.taskmanager.service.SiteService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class SiteFacadeImpl implements SiteFacade {

    private final AppUserService appUserService;
    private final SiteService siteService;

    @Override
    public BaseDto save(Authentication authentication,
                        SiteDto siteDto) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Site site = siteService.save(appUser, (Site) siteDto.toBaseEntity());
        appUserService.updateLastActionTime(appUser);
        return site.toBaseDto();
    }

    @Override
    public List<BaseDto> findAll(Authentication authentication) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        return siteService
                .findAll(appUser)
                .stream()
                .map(Site::toBaseDto)
                .toList();
    }

    @Override
    public BaseDto findById(Authentication authentication,
                            Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        return siteService
                .findById(appUser, id)
                .toBaseDto();
    }

    @Override
    public Long deleteById(Authentication authentication,
                           Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);

        siteService.delete(appUser, id);
        appUserService.updateLastActionTime(appUser);
        return id;
    }
}
