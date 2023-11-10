package io.channel.hackytalky.domain.doongdoong.entity;

import io.channel.hackytalky.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Doongdoong")
public class Doongdoong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private User owner;

    @Column(nullable = false)
    private Long level;

    @Column(nullable = false)
    private Long experiment;

    @Builder
    public Doongdoong(Long id, User owner, Long level, Long experiment) {
        this.id = id;
        this.owner = owner;
        this.level = level;
        this.experiment = experiment;
    }
}
