package io.channel.hackytalky.domain.mission.service;

import io.channel.hackytalky.domain.mission.dto.MissionAddDTO;
import io.channel.hackytalky.domain.mission.entity.Mission;
import io.channel.hackytalky.domain.mission.repository.MissionRepository;
import io.channel.hackytalky.global.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.channel.hackytalky.global.response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class MissionService {
    private final MissionRepository missionRepository;

    public Long getMissionExperiment(Long missionId) {
        Mission mission = missionRepository.getMissionById(missionId)
                .orElseThrow(() -> new BaseException(MISSION_NOT_EXIST));

        return mission.getExperiment();
    }

    public void addMission(MissionAddDTO missionAddDTO) {
        Mission mission = Mission.builder()
                .content(missionAddDTO.getContent())
                .experiment(missionAddDTO.getExperiment())
                .requireImage(missionAddDTO.getRequireImage())
                .build();

        try {
            missionRepository.save(mission);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }
    }
}
