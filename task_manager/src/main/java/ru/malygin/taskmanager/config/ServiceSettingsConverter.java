package ru.malygin.taskmanager.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.entity.ResourceSetting;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@RequiredArgsConstructor
@Converter
public class ServiceSettingsConverter implements AttributeConverter<ResourceSetting, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(ResourceSetting resourceSetting) {
        String serviceSettingsJson = "";
        try {
            serviceSettingsJson = objectMapper.writeValueAsString(resourceSetting);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error: {}", e.getMessage());
        }
        return serviceSettingsJson;
    }

    @Override
    public ResourceSetting convertToEntityAttribute(String s) {
        try {
            ServiceType type = ServiceType.valueOf(objectMapper
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
            log.error("Error parse value from database: {}", e.getMessage());
        }
        return null;
    }
}
