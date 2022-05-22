package ru.malygin.searcher.model.entity;

import ru.malygin.searcher.model.dto.BaseDto;

public interface BaseEntity {
    BaseDto toBaseDto();

    boolean hasRequiredField();
}
