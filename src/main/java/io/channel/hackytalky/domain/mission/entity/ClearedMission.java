package io.channel.hackytalky.domain.mission.entity;

import io.channel.hackytalky.domain.image.entity.Image;
import io.channel.hackytalky.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "cleared_mission")
public class ClearedMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cleared_time")
    private Timestamp clearedTime;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Image image;

    @Builder
    public ClearedMission(Timestamp clearedTime, User user, Image image) {
        this.clearedTime = clearedTime;
        this.user = user;
        this.image = image;
    }
}
