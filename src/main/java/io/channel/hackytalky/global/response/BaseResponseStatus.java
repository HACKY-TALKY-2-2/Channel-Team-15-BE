package io.channel.hackytalky.global.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    /**
     * 성공 코드 2xx
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    /**
     * Client Error - 4xx 에러
     */
    UNAUTHORIZED_ACCESS(false, HttpStatus.UNAUTHORIZED.value(), "접근 권한이 없습니다."),
    EMAIL_EXIST(false, HttpStatus.CONFLICT.value(), "해당 이메일로 가입한 회원이 존재합니다."),
    WRONG_PASSWORD(false, HttpStatus.BAD_REQUEST.value(), "잘못된 비밀번호 입니다."),
    USER_NOT_EXIST(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 회원입니다"),
    NOT_ENROLLED(false, HttpStatus.NOT_FOUND.value(), "등록되지 않은 일정입니다"),
    NOT_BOOKED(false, HttpStatus.NOT_FOUND.value(), "등록되지 않은 일정입니다"),
    NOT_SUBSCRIBED(false, HttpStatus.NOT_FOUND.value(), "등록되지 않은 카테고리입니다"),
    ALREADY_ENROLLED(false, HttpStatus.BAD_REQUEST.value(), "이미 신청한 일정입니다."),
    ALREADY_BOOKED(false, HttpStatus.BAD_REQUEST.value(), "이미 관심등록한 일정입니다."),
    ALREADY_SUBSCRIBED(false, HttpStatus.BAD_REQUEST.value(), "이미 관심등록한 카테고리입니다."),

    SCHEDULE_NOT_EXIST(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 일정입니다."),
    SCHEDULE_EXIST(false, HttpStatus.CONFLICT.value(), "해당 일정이 존재합니다."),

    CATEGORY_NOT_EXIST(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 카테고리입니다"),
    CATEGORY_EXIST(false, HttpStatus.CONFLICT.value(), "해당 카테고리가 존재합니다."),

    EXPIRED_JWT_TOKEN(false, HttpStatus.UNAUTHORIZED.value(), "만료된 토큰입니다"),
    INVALID_JWT_TOKEN(false, HttpStatus.NOT_ACCEPTABLE.value(), "유효하지 않은 토큰입니다."),
    NO_JWT_TOKEN(false, HttpStatus.NOT_ACCEPTABLE.value(), "토큰이 존재하지 않습니다."),

    /**
     * Server Error - 5xx 에러
     */
    DATABASE_INSERT_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 저장에 실패하였습니다."),
    DATABASE_DELETE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 삭제에 실패하였습니다."),
    INTERNAL_SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "알 수 없는 에러가 발생하였습니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;

    /**
     * isSuccess : 요청의 성공 또는 실패
     * code : Http Status Code
     * message : 설명
     */
    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
