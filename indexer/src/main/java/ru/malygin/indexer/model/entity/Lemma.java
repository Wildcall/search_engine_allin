package ru.malygin.indexer.model.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import ru.malygin.indexer.model.dto.BaseDto;
import ru.malygin.indexer.model.dto.impl.LemmaDto;
import ru.malygin.indexer.model.entity.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("_lemma")
public class Lemma implements BaseEntity {
    @Id
    private Long id;
    private Long siteId;
    private Long appUserId;
    private String word;
    private Integer frequency;

    @Override
    public BaseDto toBaseDto() {
        //  @formatter:off
        return new LemmaDto(id,
                            siteId,
                            appUserId,
                            word,
                            frequency);
        //  @formatter:on
    }

    @Override
    public boolean hasRequiredField() {
        //  @formatter:off
        return siteId != null
                && appUserId != null
                && word != null
                && frequency != null;
        //  @formatter:on
    }
}
