package io.channel.hackytalky.domain.doongdoong.service;

import io.channel.hackytalky.domain.doongdoong.dto.ClearMissionResponseDTO;
import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import io.channel.hackytalky.domain.image.entity.Image;
import io.channel.hackytalky.domain.image.service.ImageService;
import io.channel.hackytalky.domain.mission.entity.ClearedMission;
import io.channel.hackytalky.domain.mission.repository.ClearedMissionRepository;
import io.channel.hackytalky.domain.mission.service.MissionService;
import io.channel.hackytalky.domain.user.entity.User;
import io.channel.hackytalky.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static io.channel.hackytalky.global.response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class DoongdoongService {
    private final MissionService missionService;
    private final ImageService imageService;
    private final ClearedMissionRepository clearedMissionRepository;

    public ClearMissionResponseDTO clearMission(User user, Long missionId, MultipartFile imageFile) {
        Doongdoong doongdoong = user.getDoongdoong();
        long currentExperiment = doongdoong.getExperiment();
        long missionExperiment = missionService.getMissionExperiment(missionId);
        Image image = imageService.saveImage(imageFile);

        currentExperiment += missionExperiment;
        doongdoong.setExperiment(currentExperiment);

        long increasedLevel = checkLevelUp(doongdoong);

        try {
            ClearedMission clearedMission = ClearedMission.builder()
                    .clearedTime(Timestamp.valueOf(LocalDateTime.now()))
                    .user(user)
                    .image(image)
                    .build();

            clearedMissionRepository.save(clearedMission);

        } catch(Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }

        return new ClearMissionResponseDTO(
                increasedLevel > 0,
                increasedLevel);
    }

    private Long checkLevelUp(Doongdoong doongdoong) {
        if(doongdoong.getExperiment() < 100) {
            return (long)0;
        }

        long currentLevel = doongdoong.getLevel();
        long currentExperiment = doongdoong.getExperiment();
        long levelUp = doongdoong.getExperiment() / 100;

        currentLevel += levelUp;
        currentExperiment = currentExperiment % 100;

        doongdoong.setLevel(currentLevel);
        doongdoong.setExperiment(currentExperiment);

        return levelUp;
    }
}
