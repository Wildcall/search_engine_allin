package ru.malygin.searcher.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@With
@Table("_lemma")
public class Lemma implements BaseEntity {
    @Id
    private Long id;
    private Long siteId;
    private Long appUserId;
    private String word;
    private Integer frequency;

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
