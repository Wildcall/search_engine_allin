package ru.malygin.searcher.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.searcher.model.dto.BaseDto;
import ru.malygin.searcher.model.dto.view.View;
import ru.malygin.searcher.model.entity.BaseEntity;
import ru.malygin.searcher.model.entity.impl.Page;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto implements BaseDto {

    @JsonView(View.Response.class)
    private Long id;

    @JsonView(View.Response.class)
    private Long siteId;

    @JsonView(View.Response.class)
    private Long appUserId;

    @JsonView(View.Response.class)
    private String path;

    @JsonView(View.Response.class)
    private Integer code;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(View.Response.class)
    private LocalDateTime createTime;

    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Page(id,
                        siteId,
                        appUserId,
                        path,
                        null,
                        code,
                        createTime);
        //  @formatter:on
    }
}
