package io.channel.hackytalky.domain.doongdoong.dto;

import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class DoongdoongInfoDTO {
    private Long id;

    private Long level;

    private Long experiment;

    private String ownerName;
}


