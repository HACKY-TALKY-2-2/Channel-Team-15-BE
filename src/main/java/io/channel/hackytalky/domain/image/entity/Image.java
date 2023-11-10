package io.channel.hackytalky.domain.image.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public Image(long id, byte[] imageFile) {
        this.id = id;
        this.imageFile = imageFile;
    }
}
