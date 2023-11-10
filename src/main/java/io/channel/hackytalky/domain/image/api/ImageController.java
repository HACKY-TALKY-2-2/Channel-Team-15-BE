package io.channel.hackytalky.domain.image.api;

import io.channel.hackytalky.domain.image.repository.ImageRepository;
import io.channel.hackytalky.domain.mission.dto.MissionAddDTO;
import io.channel.hackytalky.global.exception.BaseException;
import io.channel.hackytalky.global.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {
    private final ImageRepository imageRepository;

    @GetMapping("/get/{imageId}")
    public BaseResponse<?> addMission(@PathVariable Long imageId) {
        try {
            return new BaseResponse<>(imageRepository.getImageById(imageId).getImageFile());
        } catch(BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }
}
