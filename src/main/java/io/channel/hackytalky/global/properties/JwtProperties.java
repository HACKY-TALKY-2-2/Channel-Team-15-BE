package io.channel.hackytalky.global.properties;

public class JwtProperties {
    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일
    public static final String JWT_ACCESS_TOKEN_HEADER_NAME = "Authorization";
    public static final String JWT_ACCESS_TOKEN_TYPE = "Bearer ";
    public static final String JWT_TOKEN = "JWT_TOKEN:";
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String EMPTY_STRING = "";
}
