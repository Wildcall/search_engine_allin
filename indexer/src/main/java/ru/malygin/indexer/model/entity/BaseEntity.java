package ru.malygin.indexer.model.entity;

import ru.malygin.indexer.model.dto.BaseDto;

public interface BaseEntity {
    BaseDto toBaseDto();

    boolean hasRequiredField();
}
