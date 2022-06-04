package ru.malygin.registration.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.malygin.registration.model.dto.view.AppUserView;
import ru.malygin.registration.model.entity.AppUser;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppUserDto {

    @Null(groups = {AppUserView.New.class})
    @JsonView(AppUserView.Response.class)
    private Long id;

    @NotNull(groups = {AppUserView.New.class})
    @NotBlank(groups = {AppUserView.New.class})
    @NotEmpty(groups = {AppUserView.New.class})
    @Min(value = 8, groups = {AppUserView.New.class})
    private String password;

    @NotNull(groups = {AppUserView.New.class})
    @NotBlank(groups = {AppUserView.New.class})
    @NotEmpty(groups = {AppUserView.New.class})
    @Email(regexp = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$",
            groups = {AppUserView.New.class})
    @JsonView(AppUserView.Response.class)
    private String email;

    @Null(groups = {AppUserView.New.class})
    @JsonView(AppUserView.Response.class)
    private Boolean enable;

    @Null(groups = {AppUserView.New.class})
    @JsonView(AppUserView.Response.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    @Null(groups = {AppUserView.New.class})
    @JsonView(AppUserView.Response.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastActionTime;

    @Null(groups = {AppUserView.New.class})
    @JsonView(AppUserView.Response.class)
    private List<String> roles;

    public AppUser toAppUser() {
        //  @formatter:off
        return new AppUser(this.id,
                           this.password,
                           this.email.toLowerCase(Locale.ROOT),
                           this.enable,
                           this.createTime,
                           this.lastActionTime,
                           null);
        //  @formatter:on
    }
}
