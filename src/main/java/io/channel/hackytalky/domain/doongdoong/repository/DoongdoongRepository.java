package io.channel.hackytalky.domain.doongdoong.repository;

import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DoongdoongRepository extends JpaRepository<Doongdoong, Long> {
}
