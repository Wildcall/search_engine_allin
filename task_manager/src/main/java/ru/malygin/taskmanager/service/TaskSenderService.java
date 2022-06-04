package ru.malygin.taskmanager.service;

import ru.malygin.taskmanager.config.TaskManagerProperties;

import java.util.Map;

public interface TaskSenderService {
    void send(Map<String, Object> map,
              TaskManagerProperties.ServiceQueueProperties serviceQueueProperties,
              String action);
}
