package ru.malygin.taskmanager.service;

import ru.malygin.taskmanager.model.ResourceCallback;
import ru.malygin.taskmanager.model.entity.impl.Task;

public interface NotificationService {

    void send(Task task,
              ResourceCallback resourceCallback);
}
