package ru.malygin.notification.model.entity;

import ru.malygin.notification.model.dto.BaseDto;

public interface BaseEntity {
    BaseDto toBaseDto();
}
