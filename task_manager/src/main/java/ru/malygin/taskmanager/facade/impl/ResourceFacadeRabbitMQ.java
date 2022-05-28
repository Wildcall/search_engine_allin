package ru.malygin.taskmanager.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.rabbit.impl.TaskSender;
import ru.malygin.taskmanager.service.AppUserService;
import ru.malygin.taskmanager.service.TaskService;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
@Primary
public class ResourceFacadeRabbitMQ implements ResourceFacade {

    private final AppUserService appUserService;
    private final TaskService taskService;
    private final TaskSender taskSender;

    @Override
    public Map<String, Long> start(Authentication authentication,
                                   Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        taskSender.send(task.toBody());

        return Map.of();
    }

    @Override
    public String stop(Authentication authentication,
                       Long id) {
        return "";
    }
}
