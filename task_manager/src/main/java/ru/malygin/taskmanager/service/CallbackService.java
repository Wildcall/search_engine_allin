package ru.malygin.taskmanager.service;

import ru.malygin.taskmanager.model.ResourceCallback;

public interface CallbackService {
    void process(ResourceCallback resourceCallback);
}
