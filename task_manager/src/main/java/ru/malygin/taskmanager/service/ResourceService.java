package ru.malygin.taskmanager.service;

import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.impl.Task;

public interface ResourceService {

    boolean start(Task task);

    boolean stop(Task task);

    boolean resourceNotAvailable(ResourceType type);

}
