package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malygin.helper.model.ServiceType;
import ru.malygin.helper.model.TaskState;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;
import ru.malygin.taskmanager.repository.TaskRepository;
import ru.malygin.taskmanager.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        task.setCreateTime(LocalDateTime.now());
        if (!task.hasRequiredField()) throw new IllegalArgumentException("Failed to save task");

        List<String> resourceIdList = taskRepository.findAllResourceIdByAppUserAndSite(task
                                                                                               .getAppUser()
                                                                                               .getId(), task
                                                                                               .getSite()
                                                                                               .getId());

        if (resourceIdList.contains(task
                                            .getType()
                                            .name())) throw new BadRequestException(
                "You cannot save a task for a site that already contains a task of this type");

        return taskRepository.save(task);
    }

    @Override
    public void update(Task task) {
        //  @formatter:off
        if (!task.hasRequiredField() || task.getId() == null)
            throw new IllegalArgumentException("Failed to update task");
        taskRepository.save(task);
        //  @formatter:on
    }

    @Override
    public void resetTask(Task task,
                          TaskState state) {
        if (!task.hasRequiredField() || task.getId() == null || state == null) throw new IllegalArgumentException(
                "Failed to update task");
        task.setStartTime(null);
        task.setEndTime(null);
        task.setStatId(null);
        task.setTaskState(state);
        taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskRepository
                .findById(id)
                .orElseThrow(() -> {
                    throw new BadRequestException("Task with id " + id + " not found");
                });
    }

    @Override
    public Task findByAppUserAndId(AppUser appUser,
                                   Long id) {
        return taskRepository
                .findByIdAndAppUser(id, appUser)
                .orElseThrow(() -> {
                    throw new BadRequestException("Task with id " + id + " not found");
                });
    }

    @Override
    public List<Task> findAllByAppUser(AppUser appUser) {
        return taskRepository.findByAppUser(appUser);
    }

    @Override
    public List<Task> findAllByAppUserAndSite(AppUser appUser,
                                              Site site) {
        return taskRepository.findAllByAppUserAndSite(appUser, site);
    }

    @Override
    public List<Task> findAllByAppUserAndResourceType(AppUser appUser,
                                                      ServiceType serviceType) {
        return taskRepository.findAllByAppUserAndType(appUser, serviceType);
    }

    @Override
    public List<Task> findAllByAppUserAndTaskState(AppUser appUser,
                                                   TaskState taskState) {
        return taskRepository.findAllByAppUserAndTaskState(appUser, taskState);
    }

    @Override
    public void deleteByAppUserAndId(AppUser appUser,
                                     Long id) {
        Task task = findByAppUserAndId(appUser, id);
        taskRepository.delete(task);
    }
}
