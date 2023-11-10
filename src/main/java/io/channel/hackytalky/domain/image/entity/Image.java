package io.channel.hackytalky.domain.image.entity;

import io.channel.hackytalky.domain.mission.entity.ClearedMission;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "image")
public class Image {
    @Id
    @GeneratedValue
    private Long id;

    @Basic(fetch=FetchType.LAZY)
    @Lob
    @Column(name = "image_file", columnDefinition = "MEDIUMBLOB")
    private byte[] imageFile;

    @OneToMany(mappedBy = "image")
    private List<ClearedMission> clearedMissions;

    @Builder
    public Image(long id, byte[] imageFile) {
        this.id = id;
        this.imageFile = imageFile;
    }
}
