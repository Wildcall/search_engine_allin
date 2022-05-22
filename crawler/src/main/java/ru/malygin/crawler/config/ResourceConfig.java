package ru.malygin.crawler.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.malygin.crawler.model.ResourceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Configuration
@Validated
@ConfigurationProperties("resource")
public class ResourceConfig {

    @NotNull
    @NotEmpty
    @NotBlank
    private String secret;
    @NotNull
    private Map<String, ResourceParam> types;

    public ResourceParam getResource(ResourceType type) {
        return this.types
                .keySet()
                .stream()
                .filter(name -> name.equalsIgnoreCase(type.name()))
                .findFirst()
                .map(name -> this.types.get(name))
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Resource with name " + type.name() + " not found");
                });
    }

    @Data
    @Validated
    public static class ResourceParam {
        @NotNull
        @NotEmpty
        @NotBlank
        private String name;
        @NotNull
        @NotEmpty
        @NotBlank
        private String baseUrl;
        @NotNull
        @NotEmpty
        @NotBlank
        private String secretKey;
        @NotNull
        private Map<String, String> availablePaths;
    }
}
