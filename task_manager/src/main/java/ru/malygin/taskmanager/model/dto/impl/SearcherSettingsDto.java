package ru.malygin.taskmanager.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.view.SettingView;
import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.helper.model.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.SearcherSetting;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearcherSettingsDto implements ResourceSettingDto {

    @Null(groups = {SettingView.class})
    @JsonView(SettingView.Response.class)
    private static ServiceType type = ServiceType.SEARCHER;

    @NotNull(groups = {SettingView.New.class})
    @NotBlank(groups = {SettingView.New.class})
    @NotEmpty(groups = {SettingView.New.class})
    private String name;

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
        return new SearcherSetting();
    }
}
