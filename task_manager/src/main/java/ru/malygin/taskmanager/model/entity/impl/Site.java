package ru.malygin.taskmanager.model.entity.impl;

import lombok.*;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.SiteDto;
import ru.malygin.taskmanager.model.entity.BaseEntity;
import ru.malygin.taskmanager.model.entity.SiteStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@With
@Table(name = "_site")
public class Site implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    private LocalDateTime lastStatusTime;

    @Enumerated(EnumType.STRING)
    private SiteStatus status;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser appUser;

    @ToString.Exclude
    @OneToMany(
            mappedBy = "site",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    public Site(Long id,
                String name,
                String path) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.lastStatusTime = LocalDateTime.now();
        this.status = SiteStatus.READY;
    }

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new SiteDto(this.id,
                           this.name,
                           this.path,
                           this.lastStatusTime,
                           this.status);
        //  @formatter:on
    }

    @Override
    public boolean hasRequiredField() {
        return name != null
                && path != null
                && status != null
                && appUser != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        return id.equals(site.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
