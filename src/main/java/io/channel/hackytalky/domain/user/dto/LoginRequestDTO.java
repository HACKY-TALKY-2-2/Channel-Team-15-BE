package io.channel.hackytalky.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LoginRequestDTO {
    @NotNull @NotEmpty @Email
    private String email;

    @NotNull @NotEmpty
    private String password;
}
