package io.channel.hackytalky.domain.doongdoong.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClearMissionResponseDTO {
    private Boolean isLevelUp;

    private Long increasedLevel;
}
