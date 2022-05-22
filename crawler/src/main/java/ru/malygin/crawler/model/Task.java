package ru.malygin.crawler.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.malygin.crawler.model.dto.view.View;

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

    @NotNull(groups = {View.New.class})
    @Min(value = 1000, groups = {View.New.class})
    private Long eventFreqInMs;

    @NotNull(groups = {View.New.class})
    @NotEmpty(groups = {View.New.class})
    @NotBlank(groups = {View.New.class})
    private String referrer;

    @NotNull(groups = {View.New.class})
    @NotEmpty(groups = {View.New.class})
    @NotBlank(groups = {View.New.class})
    private String userAgent;

    @NotNull(groups = {View.New.class})
    @Min(value = 1, groups = {View.New.class})
    private Integer delayInMs;

    @NotNull(groups = {View.New.class})
    @Min(value = 1, groups = {View.New.class})
    private Integer reconnect;

    @NotNull(groups = {View.New.class})
    @Min(value = 1, groups = {View.New.class})
    private Integer timeOutInMs;

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
