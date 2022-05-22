package ru.malygin.taskmanager.model.entity.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_app_user")
public class AppUser implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private LocalDateTime createTime;
    private LocalDateTime lastActionTime;

    @ToString.Exclude
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Site> sites = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "appUser", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Setting> settings = new ArrayList<>();

    public AppUser(String email) {
        this.email = email;
        this.createTime = LocalDateTime.now();
        this.lastActionTime = LocalDateTime.now();
    }

    @Override
    public BaseDto toBaseDto() {
        return null;
    }

    @Override
    public boolean hasRequiredField() {
        return email != null
                && createTime != null
                && lastActionTime != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
