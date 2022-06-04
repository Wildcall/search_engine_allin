package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.facade.SettingFacade;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;
import ru.malygin.taskmanager.service.AppUserService;
import ru.malygin.taskmanager.service.SettingsService;

import java.util.List;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Service
public class SettingFacadeImpl implements SettingFacade {

    private final AppUserService appUserService;
    private final SettingsService settingsService;

    @Override
    public BaseDto save(Authentication authentication,
                        ResourceSettingDto resourceSettingDto) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        ResourceSetting resourceSetting = resourceSettingDto.toResourceSetting();
        ServiceType serviceType = resourceSetting.getType();

        Setting setting = new Setting();
        setting.setAppUser(appUser);
        setting.setResourceSetting(resourceSetting);
        setting.setName(resourceSettingDto.getName());
        setting.setType(serviceType);

        settingsService.save(appUser, setting);
        appUserService.updateLastActionTime(appUser);
        return setting.toBaseDto();
    }

    @Override
    public List<BaseDto> findAllByResourceStringType(Authentication authentication,
                                                     String type) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        ServiceType serviceType = convertStringTypeToResourceType(type);
        return settingsService
                .findAllByResourceType(appUser, serviceType)
                .stream()
                .map(Setting::toBaseDto)
                .toList();
    }

    @Override
    public List<BaseDto> findAll(Authentication authentication) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        return settingsService
                .findAll(appUser)
                .stream()
                .map(Setting::toBaseDto)
                .toList();
    }

    @Override
    public BaseDto findById(Authentication authentication,
                            Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        return settingsService
                .findById(appUser, id)
                .toBaseDto();
    }

    @Override
    public Long deleteById(Authentication authentication,
                           Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        settingsService.deleteSettings(appUser, id);
        appUserService.updateLastActionTime(appUser);
        return id;
    }

    private ServiceType convertStringTypeToResourceType(String type) {
        try {
            return ServiceType.valueOf(type.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            throw new BadRequestException(
                    "Type of settings: " + type + " is invalid. Use one of [crawler, indexer, searcher]");
        }
    }
}
