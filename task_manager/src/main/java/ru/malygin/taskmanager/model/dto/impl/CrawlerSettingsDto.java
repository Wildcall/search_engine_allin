package ru.malygin.taskmanager.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.view.SettingView;
import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.helper.enums.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.CrawlerSetting;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CrawlerSettingsDto implements ResourceSettingDto {

    @Null(groups = {SettingView.class})
    @JsonView(SettingView.Response.class)
    private static ServiceType type = ServiceType.CRAWLER;

    @NotNull(groups = {SettingView.New.class})
    @NotBlank(groups = {SettingView.New.class})
    @NotEmpty(groups = {SettingView.New.class})
    private String name;

    @NotNull(groups = {SettingView.New.class})
    @NotBlank(groups = {SettingView.New.class})
    @NotEmpty(groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private String referrer;

    @NotNull(groups = {SettingView.New.class})
    @NotBlank(groups = {SettingView.New.class})
    @NotEmpty(groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private String userAgent;

    @NotNull(groups = {SettingView.New.class})
    @Min(value = 1, groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private Integer delayInMs;

    @NotNull(groups = {SettingView.New.class})
    @Min(value = 1, groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private Integer reconnect;

    @NotNull(groups = {SettingView.New.class})
    @Min(value = 1, groups = {SettingView.New.class})
    @JsonView(SettingView.Response.class)
    private Integer timeOutInMs;

    @Override
    public ResourceSetting toResourceSetting() {
        //  @formatter:off
        return new CrawlerSetting(this.referrer,
                                  this.userAgent,
                                  this.delayInMs,
                                  this.reconnect,
                                  this.timeOutInMs);
        //  @formatter:on
    }

    @Override
    public ServiceType getResourceType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }
}
