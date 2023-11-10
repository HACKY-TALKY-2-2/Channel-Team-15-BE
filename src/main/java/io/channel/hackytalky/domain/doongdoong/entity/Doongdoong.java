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

    @Column(nullable = false)
    private Long level;

    @Column(nullable = false)
    private Long experiment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @Builder
    public Doongdoong(long level, long experiment, User owner) {
        this.level = level;
        this.experiment = experiment;
        this.owner = owner;
    }
}
