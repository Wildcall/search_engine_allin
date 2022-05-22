package ru.malygin.searcher.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.searcher.model.dto.BaseDto;
import ru.malygin.searcher.model.dto.view.View;
import ru.malygin.searcher.model.entity.BaseEntity;
import ru.malygin.searcher.model.entity.impl.Index;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexDto implements BaseDto {

    @JsonView(View.Response.class)
    private Long id;

    @JsonView(View.Response.class)
    private Long siteId;

    @JsonView(View.Response.class)
    private Long appUserId;

    @JsonView(View.Response.class)
    private Double rank;

    @JsonView(View.Response.class)
    private String pagePath;

    @JsonView(View.Response.class)
    private String word;

    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Index(id,
                         siteId,
                         appUserId,
                         rank,
                         pagePath,
                         word);
        //  @formatter:on
    }
}
