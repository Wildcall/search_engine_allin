package ru.malygin.taskmanager.repository;

import org.springframework.data.repository.CrudRepository;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends CrudRepository<Site, Long> {

    Optional<Site> findByIdAndAppUser(Long id,
                                      AppUser appUser);

    List<Site> findAllByAppUser(AppUser appUser);

    Optional<Long> deleteByIdAndAppUser(Long id,
                                        AppUser appUser);
}
