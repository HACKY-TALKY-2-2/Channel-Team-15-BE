package io.channel.hackytalky.domain.mission.dto;

import io.channel.hackytalky.domain.mission.entity.ClearedMission;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClearedMissionDTO {
    private Long id;

    private Timestamp clearedTime;

    private Long userId;

    private Long imageId;

    public static ClearedMissionDTO of(ClearedMission clearedMission) {
        Long imageId = null;

        if(clearedMission.getImage() != null) {
            imageId = clearedMission.getImage().getId();
        }

        return new ClearedMissionDTO(
                clearedMission.getId(),
                clearedMission.getClearedTime(),
                clearedMission.getUser().getId(),
                imageId
        );
    }

}
