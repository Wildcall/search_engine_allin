package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;
import ru.malygin.taskmanager.repository.SettingRepository;
import ru.malygin.taskmanager.service.SettingsService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SettingServiceImpl implements SettingsService {

    private final SettingRepository settingRepository;

    @Override
    public Setting save(AppUser appUser,
                        Setting setting) {
        String name = setting.getName();
        if (findAll(appUser)
                .stream()
                .anyMatch(exist -> exist
                        .getName()
                        .equals(name))) throw new BadRequestException("Settings with name: " + name + " already exist");

        if (setting.hasRequiredField()) {
            log.info("SETTING SAVE / Type: {} / AppUser: {}", setting.getType(), appUser.getEmail());
            return settingRepository.save(setting);
        }
        throw new IllegalArgumentException("Failed to save setting");
    }

    @Override
    public List<Setting> findAll(AppUser appUser) {
        return settingRepository.findByAppUser(appUser);
    }

    @Override
    public List<Setting> findAllByResourceType(AppUser appUser,
                                               ResourceType resourceType) {
        return settingRepository.findByAppUserAndType(appUser, resourceType);
    }

    @Override
    public Setting findById(AppUser appUser,
                            Long id) {
        return settingRepository
                .findByIdAndAppUser(id, appUser)
                .orElseThrow(() -> {
                    throw new BadRequestException("Setting with id: " + id + " not found");
                });
    }

    @Override
    public void deleteSettings(AppUser appUser,
                               Long id) {
        Setting setting = findById(appUser, id);
        log.info("SETTING SAVE / Type: {} / Id: {} / AppUser: {}", setting.getType(), setting.getId(), appUser.getEmail());
        settingRepository.delete(setting);
    }
}
