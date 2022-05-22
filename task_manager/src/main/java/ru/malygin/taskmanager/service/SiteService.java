package ru.malygin.taskmanager.service;

import ru.malygin.taskmanager.model.entity.SiteStatus;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;

import java.util.List;

public interface SiteService {
    Site save(AppUser appUser,
              Site site);

    Site updateStatus(Site site, SiteStatus status);

    List<Site> findAll(AppUser appUser);

    Site findById(AppUser appUser,
                  Long id);

    void delete(AppUser appUser,
                Long id);
}
