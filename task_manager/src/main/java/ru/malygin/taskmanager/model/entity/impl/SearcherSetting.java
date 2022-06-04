package ru.malygin.taskmanager.model.entity.impl;

import lombok.Data;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.impl.SearcherSettingsDto;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import java.util.Map;

@Data
public class SearcherSetting implements ResourceSetting {

    private static ServiceType type = ServiceType.SEARCHER;

    @Override
    public ServiceType getType() {
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
