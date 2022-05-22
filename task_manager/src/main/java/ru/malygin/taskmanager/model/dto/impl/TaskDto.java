package ru.malygin.taskmanager.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.ResourceType;
import ru.malygin.taskmanager.model.TaskState;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.view.TaskView;
import ru.malygin.taskmanager.model.entity.BaseEntity;
import ru.malygin.taskmanager.model.entity.impl.Task;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

// TODO: 26.04.2022 Добавить отображение для TodoView.Update.class

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDto implements BaseDto {

    @Null(groups = {TaskView.New.class})
    @NotNull(groups = {TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private Long id;

    @NotNull(groups = {TaskView.New.class})
    @Min(value = 1, groups = {TaskView.New.class})
    @Null(groups = {TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private Long siteId;

    @NotNull(groups = {TaskView.New.class})
    @Min(value = 1, groups = {TaskView.New.class})
    @Null(groups = {TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private Long settingId;

    @NotNull(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private Boolean sendNotification;

    @NotNull(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private Boolean autoContinue;

    @NotNull(groups = {TaskView.New.class, TaskView.Update.class})
    @Min(value = 1000, groups = {TaskView.New.class})
    @JsonView(TaskView.Response.class)
    private Long eventFreqInMs;

    @Null(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private Long statId;

    @Null(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonView({TaskView.Response.class})
    private ResourceType type;

    @Null(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(TaskView.Response.class)
    private LocalDateTime createTime;

    @Null(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(TaskView.Response.class)
    private LocalDateTime startTime;

    @Null(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(TaskView.Response.class)
    private LocalDateTime endTime;

    @Null(groups = {TaskView.New.class, TaskView.Update.class})
    @JsonView(TaskView.Response.class)
    private TaskState taskState;

    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Task(id,
                        sendNotification,
                        autoContinue,
                        eventFreqInMs,
                        statId,
                        createTime,
                        startTime,
                        endTime,
                        taskState);
        //  @formatter:on
    }
}
