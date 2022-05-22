package ru.malygin.taskmanager.model.entity;

import ru.malygin.taskmanager.model.dto.BaseDto;

public interface BaseEntity {
    BaseDto toBaseDto();

    boolean hasRequiredField();
}
