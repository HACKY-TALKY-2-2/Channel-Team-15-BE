package io.channel.hackytalky.domain.mission.repository;

import io.channel.hackytalky.domain.mission.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    public Optional<Mission> getMissionById(Long id);
}
