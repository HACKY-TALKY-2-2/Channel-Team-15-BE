package io.channel.hackytalky.domain.mission.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MissionAddDTO {
    @Valid @NotNull
    private String content;

    @Valid @NotNull
    private Long experiment;

    @Valid @NotNull
    private Boolean requireImage;
}
