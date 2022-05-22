package ru.malygin.taskmanager.model.dto.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.taskmanager.model.dto.BaseDto;
import ru.malygin.taskmanager.model.dto.view.SiteView;
import ru.malygin.taskmanager.model.entity.BaseEntity;
import ru.malygin.taskmanager.model.entity.SiteStatus;
import ru.malygin.taskmanager.model.entity.impl.Site;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SiteDto implements BaseDto {

    @Null(groups = {SiteView.New.class})
    @JsonView(SiteView.Response.class)
    private Long id;

    @NotNull(groups = {SiteView.New.class})
    @NotBlank(groups = {SiteView.New.class})
    @NotEmpty(groups = {SiteView.New.class})
    @JsonView(SiteView.Response.class)
    private String name;

    @NotNull(groups = {SiteView.New.class})
    @NotBlank(groups = {SiteView.New.class})
    @NotEmpty(groups = {SiteView.New.class})
    @JsonView(SiteView.Response.class)
    private String path;

    @Null(groups = {SiteView.New.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonView(SiteView.Response.class)
    private LocalDateTime lastStatusTime;

    @Null(groups = {SiteView.New.class})
    @JsonView(SiteView.Response.class)
    private SiteStatus status;

    /**
     * Copy list of parameters from {@link SiteDto} to {@link Site}
     * <br>id - {@link Long}
     * <br>name - {@link String}
     * <br>path - {@link String}
     *
     * @return {@link Site}
     */
    @Override
    public BaseEntity toBaseEntity() {
        //  @formatter:off
        return new Site(this.id,
                        this.name,
                        this.path);
        //  @formatter:on
    }
}
