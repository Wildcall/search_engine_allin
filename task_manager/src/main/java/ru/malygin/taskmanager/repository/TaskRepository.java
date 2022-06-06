package ru.malygin.taskmanager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.malygin.helper.enums.ServiceType;
import ru.malygin.helper.enums.TaskState;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.model.entity.impl.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findByAppUser(AppUser appUser);

    Optional<Task> findByIdAndAppUser(Long id,
                                      AppUser appUser);

    List<Task> findAllByAppUserAndSite(AppUser appUser,
                                       Site site);

    List<Task> findAllByAppUserAndType(AppUser appUser,
                                       ServiceType serviceType);

    @Query(nativeQuery = true, value = "select type from _task where app_user_id = :appUserId and site_id = :siteId")
    List<String> findAllResourceIdByAppUserAndSite(@Param("appUserId") Long appUserId,
                                                   @Param("siteId") Long siteId);

    List<Task> findAllByAppUserAndTaskState(AppUser appUser,
                                            TaskState taskState);
}
