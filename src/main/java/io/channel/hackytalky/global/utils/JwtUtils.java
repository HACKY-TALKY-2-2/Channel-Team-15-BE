package io.channel.hackytalky.global.utils;

import io.channel.hackytalky.domain.user.entity.User;
import io.channel.hackytalky.global.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.channel.hackytalky.global.properties.JwtProperties.*;
import static io.channel.hackytalky.global.response.BaseResponseStatus.*;

@PropertySource("classpath:application.properties")
@Component
public class  JwtUtils {
    private final Key key;

    /**
     * todo: secret key 재생성
     * @param jwtSecret
     */
    public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    private String createAccessToken(String userEmail, String userName) {
        return Jwts.builder()
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                .claim("userEmail", userEmail)
                .claim("userName", userName)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, String> generateAccessToken(String userEmail, String userName) {
        String accessToken = createAccessToken(userEmail, userName);

        Map<String, String> token = new HashMap<>();
        token.put(ACCESS_TOKEN, accessToken);

        return token;
    }

    /**
     *
     * @param user
     * @return Map<accessToken, refreshToken>
     */
    public Map<String, String> generateAccessTokenWithRefreshToken(User user) {
        Map<String, String> token = generateAccessToken(user.getEmail(), user.getName());

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        token.put(REFRESH_TOKEN, refreshToken);

        return token;
    }

    public String getUserEmail(String accessToken) throws BaseException {
        return (String)getValueFromToken(accessToken, false, "userEmail");
    }

    public String getUserName(String accessToken) throws BaseException {
        return (String)getValueFromToken(accessToken, false, "userName");
    }

    public Date getExpiration(String accessToken) throws BaseException {
        return (Date)getValueFromToken(accessToken, true, "");
    }

    private Object getValueFromToken(String token, boolean isExpiration, String claimName) throws BaseException {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if(isExpiration) {
                return claims.getExpiration();
            } else {
                return claims.get(claimName);
            }
        } catch (ExpiredJwtException exception) {
            throw new BaseException(EXPIRED_JWT_TOKEN);
        } catch (Exception e) {
            throw new BaseException(INVALID_JWT_TOKEN);
        }
    }
}
