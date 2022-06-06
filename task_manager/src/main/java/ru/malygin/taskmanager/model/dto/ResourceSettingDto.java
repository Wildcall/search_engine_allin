package ru.malygin.taskmanager.model.dto;

import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.helper.enums.ServiceType;

public interface ResourceSettingDto {
    ServiceType getResourceType();

    String getName();

    ResourceSetting toResourceSetting();
}
