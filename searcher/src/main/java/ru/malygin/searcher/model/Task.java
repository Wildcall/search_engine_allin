package ru.malygin.searcher.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.helper.model.NodeTask;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task implements NodeTask, Serializable {

    private Long id;
    private Long appUserId;
    private Long siteId;
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
