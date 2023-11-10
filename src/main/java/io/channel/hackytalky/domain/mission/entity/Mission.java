package io.channel.hackytalky.domain.mission.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "mission")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "experiment")
    private Long experiment;

    @Column(name = "content")
    private String content;

    @Column(name = "require_image")
    private Boolean requireImage;

    @Builder
    public Mission(long experiment, String content, boolean requireImage) {
        this.experiment = experiment;
        this.content = content;
        this.requireImage = requireImage;
    }
}
