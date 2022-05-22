package ru.malygin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.malygin.model.ResourceType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("resource")
@Validated
public class ResourceConfig {

    @NotNull
    private Map<String, ResourceParam> types;
    @NotNull
    @NotEmpty
    @NotBlank
    private String secret;
    @NotNull
    private Expiration expiration;

    public ResourceParam getResource(ResourceType type) {
        return this.types
                .keySet()
                .stream()
                .filter(name -> name.equalsIgnoreCase(type.name()))
                .findFirst()
                .map(key -> this.types.get(key))
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
    }

    @Data
    @Validated
    public static class Expiration {
        @NotNull
        private Long access;
        @NotNull
        private Long refresh;
        @NotNull
        private Long confirm;
    }
}
