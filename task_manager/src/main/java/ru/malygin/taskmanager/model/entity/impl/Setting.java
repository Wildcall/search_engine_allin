package ru.malygin.taskmanager.model.entity.impl;

import lombok.*;
import ru.malygin.taskmanager.config.ServiceSettingsConverter;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.SettingDto;
import ru.malygin.taskmanager.model.entity.BaseEntity;
import ru.malygin.taskmanager.model.entity.ResourceSetting;
import ru.malygin.taskmanager.model.ResourceType;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_setting")
public class Setting implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    @Convert(converter = ServiceSettingsConverter.class)
    private ResourceSetting resourceSetting;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new SettingDto(this.id,
                              this.name,
                              this.type,
                              this.resourceSetting.toResourceSettingDto());
        //  @formatter:on
    }

    @Override
    public boolean hasRequiredField() {
        return name != null
                && appUser != null
                && type != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Setting setting = (Setting) o;

        return id.equals(setting.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
