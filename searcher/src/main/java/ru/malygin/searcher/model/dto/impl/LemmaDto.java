package ru.malygin.searcher.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.searcher.model.dto.BaseDto;
import ru.malygin.searcher.model.dto.view.View;
import ru.malygin.searcher.model.entity.BaseEntity;
import ru.malygin.searcher.model.entity.impl.Lemma;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LemmaDto implements BaseDto {

    @JsonView(View.Response.class)
    private Long id;

    @JsonView(View.Response.class)
    private Long siteId;

    @JsonView(View.Response.class)
    private Long appUserId;

    @JsonView(View.Response.class)
    private String word;

    @JsonView(View.Response.class)
    private Integer frequency;

    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Lemma(id,
                         siteId,
                         appUserId,
                         word,
                         frequency);
        //  @formatter:on
    }
}
