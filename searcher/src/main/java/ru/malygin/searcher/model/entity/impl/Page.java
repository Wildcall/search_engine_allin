package ru.malygin.searcher.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.malygin.searcher.model.dto.BaseDto;
import ru.malygin.searcher.model.dto.impl.PageDto;
import ru.malygin.searcher.model.entity.BaseEntity;

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
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new PageDto(id,
                           siteId,
                           appUserId,
                           path,
                           code,
                           createTime);
        //  @formatter:on
    }

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
