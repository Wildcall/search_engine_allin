package ru.malygin.taskmanager.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.view.SettingView;
import ru.malygin.taskmanager.model.entity.BaseEntity;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.Setting;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettingDto implements BaseDto {

    @JsonView({SettingView.Response.class})
    private Long id;

    @JsonView({SettingView.Response.class})
    private String name;

    @JsonView({SettingView.Response.class})
    private ServiceType type;

    @JsonView({SettingView.Response.class})
    private ResourceSettingDto resourceSettingDto;

    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Setting(this.id,
                           this.resourceSettingDto.getName(),
                           this.type,
                           resourceSettingDto.toResourceSetting(),
                           null);
        //  @formatter:on
    }
}
