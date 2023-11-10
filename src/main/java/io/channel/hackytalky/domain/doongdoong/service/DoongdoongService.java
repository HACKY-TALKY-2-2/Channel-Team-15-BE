package io.channel.hackytalky.domain.doongdoong.service;

import io.channel.hackytalky.domain.doongdoong.dto.DoongdoongInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class DoongdoongService {
    public DoongdoongInfoDTO getDoongdoongInfo() {
        return new DoongdoongInfoDTO((long)1, (long)1, (long)1, "TEST");
    }
}
