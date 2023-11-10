package io.channel.hackytalky.domain.user.service;

import io.channel.hackytalky.domain.doongdoong.dto.ClearMissionResponseDTO;
import io.channel.hackytalky.domain.doongdoong.dto.DoongdoongInfoDTO;
import io.channel.hackytalky.domain.doongdoong.entity.Doongdoong;
import io.channel.hackytalky.domain.doongdoong.repository.DoongdoongRepository;
import io.channel.hackytalky.domain.doongdoong.service.DoongdoongService;
import io.channel.hackytalky.domain.user.dto.LoginRequestDTO;
import io.channel.hackytalky.domain.user.dto.SignupRequestDTO;
import io.channel.hackytalky.domain.user.entity.User;
import io.channel.hackytalky.domain.user.repository.UserRepository;
import io.channel.hackytalky.global.exception.BaseException;
import io.channel.hackytalky.global.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static io.channel.hackytalky.global.properties.JwtProperties.*;
import static io.channel.hackytalky.global.response.BaseResponseStatus.*;


@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final DoongdoongRepository doongdoongRepository;
    private final DoongdoongService doongdoongService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    public void signup(SignupRequestDTO signupRequestDTO) throws BaseException {
        userRepository.findUserByEmail(signupRequestDTO.getEmail())
                .ifPresent(s -> {
                    throw new BaseException(EMAIL_EXIST);
                });

        User userEntity = User.builder()
                .name(signupRequestDTO.getName())
                .email(signupRequestDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(signupRequestDTO.getPassword()))
                .build();

        Doongdoong doongdoongEntity = Doongdoong.builder()
                .level((long)0)
                .experiment((long)0)
                .owner(userEntity)
                .build();

        try {
            userRepository.save(userEntity);
            doongdoongRepository.save(doongdoongEntity);
        } catch (Exception e) {
            throw new BaseException(DATABASE_INSERT_ERROR);
        }
    }

    public void login(LoginRequestDTO loginRequestDTO, HttpServletResponse response) throws BaseException {
        User user = userRepository.findUserByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new BaseException(USER_NOT_EXIST));

        if (!bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            throw new BaseException(WRONG_PASSWORD);
        }

        Map<String, String> token = jwtUtils.generateAccessTokenWithRefreshToken(user);

        response.addHeader(JWT_ACCESS_TOKEN_HEADER_NAME, JWT_ACCESS_TOKEN_TYPE + token.get(ACCESS_TOKEN));

        Cookie cookie = new Cookie(REFRESH_TOKEN, token.get(REFRESH_TOKEN));
        cookie.setMaxAge((int) REFRESH_TOKEN_EXPIRE_TIME);
        cookie.setPath("/");

        response.addCookie(cookie);

        Duration duration = Duration.of(ACCESS_TOKEN_EXPIRE_TIME, ChronoUnit.MILLIS);
        redisTemplate.opsForValue().set(JWT_TOKEN + user.getEmail(), token.get(ACCESS_TOKEN), duration);
    }

    public void logout(HttpServletRequest request) throws BaseException {
        User user = getUser(request);
        String userEmail = user.getEmail();

        if (redisTemplate.opsForValue().get(JWT_TOKEN + userEmail) != null) {
            redisTemplate.delete(JWT_TOKEN + userEmail);
        }
    }

    public void withDraw(HttpServletRequest request, String password) throws BaseException {
        User user = getUser(request);

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new BaseException(WRONG_PASSWORD);
        }

        logout(request);

        Doongdoong doongdoong = user.getDoongdoong();

        try {
            doongdoongRepository.delete(doongdoong);
            userRepository.delete(user);
        } catch (BaseException e) {
            throw new BaseException(DATABASE_DELETE_ERROR);
        }
    }

    public DoongdoongInfoDTO getDoongdoongInformation(HttpServletRequest request) {
        User user = getUser(request);

        return DoongdoongInfoDTO.of(user.getDoongdoong());
    }

    public ClearMissionResponseDTO clearMission(HttpServletRequest request, Long experiment) {
        User user = getUser(request);

        return doongdoongService.clearMission(user.getDoongdoong(), experiment);
    }

    private User getUser(HttpServletRequest request) throws BaseException {
        String jwtHeader = request.getHeader(JWT_ACCESS_TOKEN_HEADER_NAME);

        if (jwtHeader == null || !jwtHeader.startsWith(JWT_ACCESS_TOKEN_TYPE)) {
            throw new BaseException(NO_JWT_TOKEN);
        }

        String jwtToken = jwtHeader.replace(JWT_ACCESS_TOKEN_TYPE, EMPTY_STRING);
        String userEmail = jwtUtils.getUserEmail(jwtToken);

        return userRepository.findUserByEmail(userEmail).orElseThrow(
                () -> new BaseException(USER_NOT_EXIST)
        );
    }
}
