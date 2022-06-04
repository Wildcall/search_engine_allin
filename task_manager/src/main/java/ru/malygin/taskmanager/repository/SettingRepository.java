package ru.malygin.taskmanager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.malygin.taskmanager.model.ServiceType;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Setting;

import java.util.List;
import java.util.Optional;

public interface SettingRepository extends CrudRepository<Setting, Long> {

    Optional<Setting> findByIdAndAppUser(Long id,
                                         AppUser appUser);

    List<Setting> findByAppUserAndType(AppUser appUser,
                                       ServiceType serviceType);

    List<Setting> findByAppUser(AppUser appUser);
}
