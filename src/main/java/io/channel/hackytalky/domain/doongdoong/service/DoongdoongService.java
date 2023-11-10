package io.channel.hackytalky.domain.doongdoong.service;

import io.channel.hackytalky.domain.doongdoong.dto.ClearMissionResponseDTO;
import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import io.channel.hackytalky.domain.image.service.ImageService;
import io.channel.hackytalky.domain.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class DoongdoongService {
    private final MissionService missionService;
    private final ImageService imageService;

    public ClearMissionResponseDTO clearMission(Doongdoong doongdoong, Long missionId, MultipartFile imageFile) {
        long currentExperiment = doongdoong.getExperiment();
        long missionExperiment = missionService.getMissionExperiment(missionId);
        imageService.saveImage(imageFile);

        currentExperiment += missionExperiment;
        doongdoong.setExperiment(currentExperiment);

        long increasedLevel = checkLevelUp(doongdoong);

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
