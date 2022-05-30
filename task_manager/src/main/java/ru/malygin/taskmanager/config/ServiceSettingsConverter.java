package ru.malygin.taskmanager.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.malygin.helper.service.LogSender;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@RequiredArgsConstructor
@Converter
public class ServiceSettingsConverter implements AttributeConverter<ResourceSetting, String> {

    private final ObjectMapper objectMapper;
    private final LogSender logSender;

    @Override
    public String convertToDatabaseColumn(ResourceSetting resourceSetting) {
        String serviceSettingsJson = "";
        try {
            serviceSettingsJson = objectMapper.writeValueAsString(resourceSetting);
        } catch (final JsonProcessingException e) {
            logSender.error("JSON writing error: %s", e);
        }
        return serviceSettingsJson;
    }

    @Override
    public ResourceSetting convertToEntityAttribute(String s) {
        try {
            ResourceType type = ResourceType.valueOf(objectMapper
                                                             .readTree(s)
                                                             .get("type")
                                                             .asText());
            return objectMapper.readValue(s, ResourceSetting.classMap
                    .keySet()
                    .stream()
                    .filter(k -> k.equals(type))
                    .findFirst()
                    .map(ResourceSetting.classMap::get)
                    .orElseThrow());
        } catch (JsonProcessingException e) {
            logSender.error("Error parse value from database: %s", e.getMessage());
        }
        return null;
    }
}
