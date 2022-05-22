package ru.malygin.taskmanager.service;

import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;

import java.util.List;

public interface SettingsService {

    Setting save(AppUser appUser,
                 Setting setting);

    List<Setting> findAll(AppUser appUser);

    List<Setting> findAllByResourceType(AppUser appUser,
                                        ResourceType resourceType);

    Setting findById(AppUser appUser,
                     Long id);

    void deleteSettings(AppUser appUser,
                        Long id);

}
