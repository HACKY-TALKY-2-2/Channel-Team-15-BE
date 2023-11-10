package io.channel.hackytalky.domain.image.service;

import io.channel.hackytalky.domain.image.entity.Image;
import io.channel.hackytalky.domain.image.repository.ImageRepository;
import io.channel.hackytalky.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static io.channel.hackytalky.global.response.BaseResponseStatus.DATABASE_INSERT_ERROR;
import static io.channel.hackytalky.global.response.BaseResponseStatus.NOT_IMAGE_FORMAT;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    public void saveImage(MultipartFile imageFile) {
        if(imageFile != null) {
            byte[] imageBytes;

            try {
                imageBytes = imageFile.getBytes();
            } catch (Exception e) {
                throw new BaseException(NOT_IMAGE_FORMAT);
            }

            try {
                Image image = Image.builder()
                        .imageFile(imageBytes)
                        .build();

                imageRepository.save(image);
            } catch (Exception e) {
                throw new BaseException(DATABASE_INSERT_ERROR);
            }
        }
    }
}
