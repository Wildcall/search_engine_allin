package ru.malygin.taskmanager.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.impl.IndexerSettingsDto;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexerSetting implements ResourceSetting {

    private static ServiceType type = ServiceType.INDEXER;
    private Map<String, Double> selectorWeight;
    private Integer parallelism;

    @Override
    public ServiceType getType() {
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
