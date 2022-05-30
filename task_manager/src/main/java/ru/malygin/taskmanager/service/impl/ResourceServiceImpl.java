package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.ResourceService;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResourceServiceImpl implements ResourceService {
    @Override
    public boolean start(Task task) {
        return false;
    }

    @Override
    public boolean stop(Task task) {
        return false;
    }

    @Override
    public boolean resourceNotAvailable(ResourceType type) {
        return false;
    }
}
