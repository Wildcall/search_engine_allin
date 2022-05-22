package ru.malygin.indexer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.malygin.indexer.model.dto.view.View;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class Task {

    @NotNull(groups = {View.New.class})
    @Min(value = 1, groups = {View.New.class})
    private Long id;

    @NotNull(groups = {View.New.class})
    @Min(value = 1, groups = {View.New.class})
    private Long appUserId;

    @NotNull(groups = {View.New.class})
    @Min(value = 1, groups = {View.New.class})
    private Long siteId;

    @NotNull(groups = {View.New.class})
    @NotEmpty(groups = {View.New.class})
    @NotBlank(groups = {View.New.class})
    private String path;

    @NotNull(groups = {View.New.class})
    @Min(value = 1000, groups = {View.New.class})
    private Long eventFreqInMs;

    @NotNull(groups = {View.New.class})
    private Integer parallelism;

    @NotNull(groups = {View.New.class})
    private Map<String, Double> selectorWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
