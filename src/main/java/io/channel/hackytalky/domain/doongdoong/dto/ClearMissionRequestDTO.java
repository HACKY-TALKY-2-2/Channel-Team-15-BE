package io.channel.hackytalky.domain.doongdoong.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ClearMissionRequestDTO {
    @NotNull
    private Long missionId;
}
