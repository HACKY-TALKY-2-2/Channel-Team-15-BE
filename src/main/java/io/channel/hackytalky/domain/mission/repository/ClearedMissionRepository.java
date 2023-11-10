package io.channel.hackytalky.domain.mission.repository;

import io.channel.hackytalky.domain.mission.entity.ClearedMission;
import io.channel.hackytalky.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClearedMissionRepository extends JpaRepository<ClearedMission, Long> {
    public List<ClearedMission> getClearedMissionsByUser(User user);
}
