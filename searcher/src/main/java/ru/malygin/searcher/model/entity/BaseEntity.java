package ru.malygin.searcher.model.entity;

import java.io.Serializable;

public interface BaseEntity extends Serializable {
    boolean hasRequiredField();
}
