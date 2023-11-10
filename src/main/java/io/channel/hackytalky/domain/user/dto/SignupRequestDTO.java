package io.channel.hackytalky.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDTO {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String password;
}
