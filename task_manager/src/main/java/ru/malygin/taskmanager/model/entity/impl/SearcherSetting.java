package ru.malygin.taskmanager.model.entity.impl;

import lombok.Data;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.impl.SearcherSettingsDto;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import java.util.Map;

@Data
public class SearcherSetting implements ResourceSetting {

    private static ResourceType type = ResourceType.SEARCHER;

    @Override
    public ResourceType getType() {
        return type;
    }

    @Override
    public ResourceSettingDto toResourceSettingDto() {
        return new SearcherSettingsDto();
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of();
    }
}
