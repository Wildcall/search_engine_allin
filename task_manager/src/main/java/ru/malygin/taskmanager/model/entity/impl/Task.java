package ru.malygin.taskmanager.model.entity.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.malygin.helper.model.ServiceType;
import ru.malygin.helper.model.TaskState;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.impl.TaskDto;
import ru.malygin.taskmanager.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "_task")
public class Task implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser appUser;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Site site;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    private Setting setting;

    @Enumerated(EnumType.STRING)
    private ServiceType type;

    private Boolean sendNotification;
    private Boolean autoContinue;
    private Long eventFreqInMs;
    private Long statId;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private TaskState taskState;

    public Task(Long id,
                Boolean sendNotification,
                Boolean autoContinue,
                Long eventFreqInMs,
                Long statId,
                LocalDateTime createTime,
                LocalDateTime startTime,
                LocalDateTime endTime,
                TaskState taskState) {
        this.id = id;
        this.sendNotification = sendNotification;
        this.autoContinue = autoContinue;
        this.eventFreqInMs = eventFreqInMs;
        this.statId = statId;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.taskState = taskState;
    }

    public boolean isStarted() {
        return startTime != null && endTime == null;
    }

    public boolean isEnded() {
        return endTime != null && startTime != null;
    }

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new TaskDto(id,
                           site.getId(),
                           setting.getId(),
                           sendNotification,
                           autoContinue,
                           eventFreqInMs,
                           statId,
                           type,
                           createTime,
                           startTime,
                           endTime,
                           taskState);
        //  @formatter:on
    }

    @Override
    public boolean hasRequiredField() {
        //  @formatter:off
        return appUser != null
                && site != null
                && setting != null
                && sendNotification != null
                && autoContinue != null
                && eventFreqInMs != null
                && type != null
                && createTime != null
                && taskState != null;
        //  @formatter:on
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return id != null && Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Map<String, Object> toBody() {
        //  @formatter:off
        Map<String, Object> bodyMap = new HashMap<>(setting.getResourceSetting().toMap());
        bodyMap.putAll(Map.of("id", id,
                              "appUserId", appUser.getId(),
                              "siteId", site.getId(),
                              "path", site.getPath(),
                              "eventFreqInMs", eventFreqInMs));
        return bodyMap;
        //  @formatter:on
    }
}
