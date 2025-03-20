package mango.security.basic_login.type;

import lombok.Getter;

@Getter
public enum ApiResponseType {
    SUCCESS("200", "성공"),
    INVALID_TOKEN("498", "유효하지 않은 토큰"),
    FORBIDDEN("403", "접근 권한 없음"),
    OUTDATED_TOKEN("419", "토큰 만료"),
    ERROR("500", "서버 오류"),
    UNAUTHORIZED("401", "인증 실패");


    private final String status;
    private final String message;

    ApiResponseType(String status, String message) {
        this.status = status;
        this.message = message;
    }
}