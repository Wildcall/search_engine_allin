package ru.malygin.searcher.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Table("_page")
public class Page implements BaseEntity {

    @Id
    private Long id;
    private Long siteId;
    private Long appUserId;
    private String path;
    private String content;
    private Integer code;
    private LocalDateTime createTime;

    @Override
    public boolean hasRequiredField() {
        //  @formatter:off
        return siteId != null
                && appUserId != null
                && path != null
                && content != null
                && code != null
                && createTime != null;
        //  @formatter:on
    }
}
