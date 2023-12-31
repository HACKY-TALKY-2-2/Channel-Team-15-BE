package io.channel.hackytalky.domain.user.entity;

import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import io.channel.hackytalky.domain.mission.entity.ClearedMission;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @OneToOne
    @JoinColumn(name = "id")
    private Doongdoong doongdoong;

    @OneToMany(mappedBy = "user")
    private List<ClearedMission> clearedMission;

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
