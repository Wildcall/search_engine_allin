package ru.malygin.taskmanager.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.malygin.helper.config.SearchEngineProperties;
import ru.malygin.helper.service.senders.TaskSender;
import ru.malygin.taskmanager.facade.ResourceFacade;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.TaskAction;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Task;
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
    private final SearchEngineProperties seProp;

    @Override
    public Map<String, Long> start(Authentication authentication,
                                   Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        String queue = getQueue(task);
        taskSender.send(task.toBody(), queue, TaskAction.START.name());
        return Map.of();

    }

    @Override
    public String stop(Authentication authentication,
                       Long id) {
        AppUser appUser = appUserService.findByAuthentication(authentication);
        Task task = taskService.findByAppUserAndId(appUser, id);
        String queue = getQueue(task);
        taskSender.send(task.toBody(), queue, TaskAction.STOP.name());
        return "";
    }

    private String getQueue(Task task) {
        //  @formatter:off
        String queue = seProp.getMsg().getCrawlerTask().getQueue();
        if (task.getType().equals(ResourceType.INDEXER))
            queue = seProp.getMsg().getIndexerTask().getQueue();
        if (task.getType().equals(ResourceType.SEARCHER))
            queue = seProp.getMsg().getSearcherTask().getQueue();
        return queue;
        //  @formatter:on
    }
}
