package ru.malygin.taskmanager.model.dto;

import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.taskmanager.model.ResourceType;

public interface ResourceSettingDto {
    ResourceType getResourceType();

    String getName();

    ResourceSetting toResourceSetting();
}
