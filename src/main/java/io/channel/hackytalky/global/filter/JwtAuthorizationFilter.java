package io.channel.hackytalky.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.channel.hackytalky.domain.user.entity.User;
import io.channel.hackytalky.domain.user.repository.UserRepository;
import io.channel.hackytalky.global.exception.BaseException;
import io.channel.hackytalky.global.response.BaseResponse;
import io.channel.hackytalky.global.security.HackyTalkyPrincipal;
import io.channel.hackytalky.global.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.channel.hackytalky.global.response.BaseResponseStatus;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;

import static io.channel.hackytalky.global.properties.JwtProperties.*;
import static io.channel.hackytalky.global.response.BaseResponseStatus.*;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String jwtHeader = request.getHeader(JWT_ACCESS_TOKEN_HEADER_NAME);

        if (jwtHeader == null || !jwtHeader.startsWith(JWT_ACCESS_TOKEN_TYPE)) {
            SetResponseAsError(response, NO_JWT_TOKEN);
            return;
        }

        String jwtToken = jwtHeader.replace(JWT_ACCESS_TOKEN_TYPE, EMPTY_STRING);
        String userEmail = null;
        String userName = null;

        try {
            userEmail = jwtUtils.getUserEmail(jwtToken);
            userName = jwtUtils.getUserName((jwtToken));
        } catch(BaseException e) {
            if(e.getStatus() == EXPIRED_JWT_TOKEN &&
                    redisTemplate.hasKey(JWT_TOKEN + userEmail))
            {
                String key = redisTemplate.opsForValue().get(JWT_TOKEN + userEmail);

                if(key.equals(jwtToken)) {
                    refreshToken(response, userEmail, userName);
                    return;
                }

                SetResponseAsError(response, INVALID_JWT_TOKEN);
                return;
            }

            else if(e.getStatus() == EXPIRED_JWT_TOKEN) {
                SetResponseAsError(response, INVALID_JWT_TOKEN);
                return;
            }

            SetResponseAsError(response, e.getStatus());
            return;
        }

        if (userEmail != null && userName != null) {
            User user;

            try {
                user = userRepository
                        .findUserByEmail(userEmail)
                        .orElseThrow(() -> new BaseException(USER_NOT_EXIST));
            } catch(BaseException e) {
                SetResponseAsError(response, e.getStatus());
                return;
            }

            if (redisTemplate.hasKey(JWT_TOKEN + userEmail)) {
                HackyTalkyPrincipal logPrincipal = HackyTalkyPrincipal.createHackyTalkyPrincipalByUserEntity(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        logPrincipal,
                        null,
                        logPrincipal.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            }

            else {
                SetResponseAsError(response, INVALID_JWT_TOKEN);
            }
        }
    }

    public void SetResponseAsError(HttpServletResponse response, BaseResponseStatus baseResponseStatus) throws IOException {
        BaseResponse<?> baseResponse = new BaseResponse<>(baseResponseStatus);
        ObjectMapper objectMapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(baseResponse.getCode());

        response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
    }

    public void refreshToken(HttpServletResponse response, String userEmail, String userName) {
        Map<String, String> token = jwtUtils.generateAccessToken(userName, userEmail);
        redisTemplate.delete(JWT_TOKEN + userEmail);
        Duration duration = Duration.of(ACCESS_TOKEN_EXPIRE_TIME, ChronoUnit.MILLIS);

        redisTemplate.opsForValue().set(JWT_TOKEN + userEmail, token.get(ACCESS_TOKEN), duration);
        response.addHeader(JWT_ACCESS_TOKEN_HEADER_NAME, JWT_ACCESS_TOKEN_TYPE + token.get(ACCESS_TOKEN));

        response.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }
}
