package ru.malygin.taskmanager.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.impl.IndexerSettingsDto;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexerSetting implements ResourceSetting {

    private static ResourceType type = ResourceType.INDEXER;
    private Map<String, Double> selectorWeight;
    private Integer parallelism;

    @Override
    public ResourceType getType() {
        return type;
    }

    @Override
    public ResourceSettingDto toResourceSettingDto() {
        //  @formatter:off
        return new IndexerSettingsDto(null,
                                      this.selectorWeight,
                                      this.parallelism);
        //  @formatter:on
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("parallelism", parallelism,
                      "selectorWeight", selectorWeight);
    }
}
