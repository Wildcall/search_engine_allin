package ru.malygin.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malygin.taskmanager.exception.BadRequestException;
import ru.malygin.taskmanager.model.entity.SiteStatus;
import ru.malygin.taskmanager.model.entity.impl.AppUser;
import ru.malygin.taskmanager.model.entity.impl.Site;
import ru.malygin.taskmanager.repository.SiteRepository;
import ru.malygin.taskmanager.service.SiteService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class SiteServiceImpl implements SiteService {

    public final SiteRepository siteRepository;

    @Override
    public Site save(AppUser appUser,
                     Site site) {
        //  @formatter:off
        if (findAll(appUser)
                .stream()
                .anyMatch(exist -> exist.getName().equals(site.getName())
                                || exist.getPath().equals(site.getPath())))
            throw new BadRequestException("Site with that name / path already exist");
        //  @formatter:on
        site.setAppUser(appUser);
        site.setLastStatusTime(LocalDateTime.now());
        site.setStatus(SiteStatus.READY);
        if (site.hasRequiredField()) {
            log.info("SITE SAVE / Path: {} / AppUser: {}", site.getPath(), appUser.getEmail());
            return siteRepository.save(site);
        }
        throw new IllegalArgumentException("Failed to save site");
    }

    @Override
    public Site updateStatus(Site site,
                             SiteStatus status) {
        if (site.hasRequiredField()) {
            site.setStatus(status);
            site.setLastStatusTime(LocalDateTime.now());
            return siteRepository.save(site);
        }
        throw new IllegalArgumentException("Failed to save site");
    }

    @Override
    public List<Site> findAll(AppUser appUser) {
        return siteRepository.findAllByAppUser(appUser);
    }

    @Override
    public Site findById(AppUser appUser,
                         Long id) {
        return siteRepository
                .findByIdAndAppUser(id, appUser)
                .orElseThrow(() -> {
                    throw new BadRequestException("Site with id: " + id + " not found");
                });
    }

    @Override
    public void delete(AppUser appUser,
                       Long id) {
        Site site = findById(appUser, id);
        log.info("SITE DELETE / Path: {} / Id: {} / AppUser: {}", site.getPath(), site.getId(), appUser.getEmail());
        siteRepository.delete(site);
    }
}
