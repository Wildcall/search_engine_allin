package ru.malygin.taskmanager.service;

import ru.malygin.helper.enums.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;

import java.util.List;

public interface SettingsService {

    Setting save(AppUser appUser,
                 Setting setting);

    List<Setting> findAll(AppUser appUser);

    List<Setting> findAllByResourceType(AppUser appUser,
                                        ServiceType serviceType);

    Setting findById(AppUser appUser,
                     Long id);

    void deleteSettings(AppUser appUser,
                        Long id);

}
