package io.channel.hackytalky.domain.doongdoong.repository;

import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import io.channel.hackytalky.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoongdoongRepository extends JpaRepository<Doongdoong, Long> {
    Optional<Doongdoong> findDoongdoongByOwner(User user);
}
