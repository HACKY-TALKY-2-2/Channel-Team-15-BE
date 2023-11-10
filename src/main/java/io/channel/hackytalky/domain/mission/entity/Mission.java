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

    @Builder
    public Mission(Long experiment, String content) {
        this.experiment = experiment;
        this.content = content;
    }
}
