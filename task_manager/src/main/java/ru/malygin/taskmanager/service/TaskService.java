package ru.malygin.taskmanager.service;

import ru.malygin.helper.model.ServiceType;
import ru.malygin.helper.model.TaskState;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;

import java.util.List;

public interface TaskService {

    Task save(Task task);

    void update(Task task);

    void resetTask(Task task,
                   TaskState state);

    Task findById(Long id);

    Task findByAppUserAndId(AppUser appUser,
                            Long id);

    List<Task> findAllByAppUser(AppUser appUser);

    List<Task> findAllByAppUserAndSite(AppUser appUser,
                                       Site site);

    List<Task> findAllByAppUserAndResourceType(AppUser appUser,
                                               ServiceType serviceType);

    List<Task> findAllByAppUserAndTaskState(AppUser appUser,
                                            TaskState taskState);

    void deleteByAppUserAndId(AppUser appUser,
                              Long id);
}
