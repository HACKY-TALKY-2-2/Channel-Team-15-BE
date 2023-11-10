package io.channel.hackytalky.domain.doongdoong.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ClearMissionRequestDTO {
    @NotBlank
    private Long missionId;
}
