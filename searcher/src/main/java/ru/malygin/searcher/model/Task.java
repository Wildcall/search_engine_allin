package ru.malygin.searcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.malygin.searcher.model.dto.view.View;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
