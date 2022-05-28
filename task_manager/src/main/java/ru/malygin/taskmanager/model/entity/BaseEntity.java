package ru.malygin.taskmanager.model.entity;

import ru.malygin.taskmanager.model.dto.BaseDto;

import java.io.Serializable;

public interface BaseEntity extends Serializable {
    BaseDto toBaseDto();

    boolean hasRequiredField();
}
