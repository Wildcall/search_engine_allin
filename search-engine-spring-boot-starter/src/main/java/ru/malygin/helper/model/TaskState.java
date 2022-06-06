package ru.malygin.taskmanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TaskState {
    CREATE(0, ""),
    START(1, "task_start"),
    COMPLETE(3, "task_end"),
    INTERRUPT(4, "task_interrupt"),
    ERROR(5, "task_error");

    private final int state;
    private final String template;

    public static TaskState getFromState(int state) {
        return Arrays
                .stream(TaskState.values())
                .filter(taskState -> taskState.getState() == state)
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
}
