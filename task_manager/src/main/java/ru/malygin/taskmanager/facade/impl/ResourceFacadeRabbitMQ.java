package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.helper.model.TaskAction;
import ru.malygin.taskmanager.config.TaskManagerProperties;
import ru.malygin.taskmanager.facade.ResourceFacade;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.service.AppUserService;
import ru.malygin.taskmanager.service.TaskSenderService;
import ru.malygin.taskmanager.service.TaskService;

import java.util.Map;
import java.util.Optional;

import static ru.malygin.taskmanager.config.TaskManagerProperties.ServiceQueueProperties;

@Slf4j
@RequiredArgsConstructor
@Service
@Primary
public class ResourceFacadeRabbitMQ implements ResourceFacade {

    private final AppUserService appUserService;
    private final TaskService taskService;
    private final TaskSenderService taskSenderService;
    private final TaskManagerProperties properties;

    @Override
    public Map<String, Long> start(Authentication authentication,
                                   Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        getQueue(task).ifPresent(serviceQueueProperties ->
                                         taskSenderService.send(task.toBody(),
                                                                serviceQueueProperties,
                                                                TaskAction.START.name()));
        return Map.of();
    }

    @Override
    public String stop(Authentication authentication,
                       Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        getQueue(task).ifPresent(serviceQueueProperties ->
                                         taskSenderService.send(task.toBody(),
                                                                serviceQueueProperties,
                                                                TaskAction.STOP.name()));

        return "";
    }

    private Optional<ServiceQueueProperties> getQueue(Task task) {
        //  @formatter:off
        if (task.getType().equals(ServiceType.CRAWLER))
            return Optional.of(properties.getCrawler());
        if (task.getType().equals(ServiceType.INDEXER))
            return Optional.of(properties.getIndexer());
        if (task.getType().equals(ServiceType.SEARCHER))
            return Optional.of(properties.getSearcher());
        return Optional.empty();
        //  @formatter:on
    }
}
