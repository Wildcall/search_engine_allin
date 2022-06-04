package ru.malygin.taskmanager.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.view.SettingView;
import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.IndexerSetting;

import javax.validation.constraints.*;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexerSettingsDto implements ResourceSettingDto {

    @Null(groups = {SettingView.class})
    @JsonView(SettingView.Response.class)
    private static ServiceType type = ServiceType.INDEXER;

    @NotNull(groups = {SettingView.New.class})
    @NotBlank(groups = {SettingView.New.class})
    @NotEmpty(groups = {SettingView.New.class})
    private String name;

    @NotNull(groups = {SettingView.New.class})
    @NotEmpty(groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private Map<String, Double> selectorWeight;

    @NotNull(groups = {SettingView.New.class})
    @Min(value = 1, groups = {SettingView.New.class})
    @Max(value = 8, groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private Integer parallelism;

    @Override
    public ServiceType getResourceType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ResourceSetting toResourceSetting() {
        //  @formatter:off
        return new IndexerSetting(this.selectorWeight,
                                  this.parallelism);
        //  @formatter:on
    }
}
