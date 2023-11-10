package io.channel.hackytalky.domain.doongdoong.dto;

import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoongdoongInfoDTO {
    private Long id;

    private Long level;

    private Long experiment;

    private String ownerName;

    public static DoongdoongInfoDTO of(Doongdoong doongdoong) {
        return new DoongdoongInfoDTO(
                doongdoong.getId(),
                doongdoong.getLevel(),
                doongdoong.getExperiment(),
                doongdoong.getOwner().getName());
    }
}


