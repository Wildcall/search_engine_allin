package ru.malygin.taskmanager.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.dto.ResourceSettingDto;
import ru.malygin.taskmanager.model.dto.impl.CrawlerSettingsDto;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlerSetting implements ResourceSetting {

    private static ResourceType type = ResourceType.CRAWLER;
    private String referrer;
    private String userAgent;
    private Integer delayInMs;
    private Integer reconnect;
    private Integer timeOutInMs;

    @Override
    public ResourceType getType() {
        return type;
    }

    @Override
    public ResourceSettingDto toResourceSettingDto() {
        return new CrawlerSettingsDto(null,
                                      this.referrer,
                                      this.userAgent,
                                      this.delayInMs,
                                      this.reconnect,
                                      this.timeOutInMs);
    }

    @Override
    public Map<String, Object> toMap() {
        return Map.of("referrer", referrer,
                      "userAgent", userAgent,
                      "delayInMs", delayInMs,
                      "reconnect", reconnect,
                      "timeOutInMs", timeOutInMs);
    }
}
