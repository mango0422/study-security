package mango.security.authorizationServer.type;

import lombok.Getter;

@Getter
public enum ApiResponseType {
    SUCCESS("200", "성공"),
    USER_NOT_FOUND("404", "사용자를 찾을 수 없음"),
    INVALID_TOKEN("498", "유효하지 않은 토큰"),
    FORBIDDEN("403", "접근 권한 없음"),
    OUTDATED_TOKEN("419", "토큰 만료"),
    ERROR("500", "서버 오류"),
    UNAUTHORIZED("401", "인증 실패"),
    LOGIN_FAILED("400", "로그인 실패"),
    REFRESH_TOKEN_EXPIRED("440", "리프레시 토큰 만료"),
    REFRESH_TOKEN_INVALID("499", "유효하지 않은 리프레시 토큰"),
    INVALID_REQUEST("422", "잘못된 요청입니다."),
    CLIENT_NOT_FOUND("404", "등록되지 않은 클라이언트입니다.");

    private final String status;
    private final String message;

    ApiResponseType(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
