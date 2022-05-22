package ru.malygin.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceCallback {

    private Long id;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long statId;
}
