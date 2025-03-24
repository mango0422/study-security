package mango.security.authorizationServer.type;

import lombok.Getter;

@Getter
public enum ApiResponseType {
    SUCCESS("200", "성공"),
    LOGIN_FAILED("400", "로그인 실패"),
    UNAUTHORIZED("401", "인증 실패"),
    FORBIDDEN("403", "접근 권한 없음"),
    USER_NOT_FOUND("404", "사용자를 찾을 수 없음"),
    CLIENT_NOT_FOUND("404", "등록되지 않은 클라이언트입니다."),
    CLIENT_ALREADY_CONSENTED("409", "이미 동의한 클라이언트입니다."),
    OUTDATED_TOKEN("419", "토큰 만료"),
    INVALID_REQUEST("422", "잘못된 요청입니다."),
    CLIENT_ID_REQUIRED("422", "client_id는 필수입니다."),
    PRINCIPAL_NAME_REQUIRED("422", "principal_name은 필수입니다."),
    SCOPE_REQUIRED("422", "scope는 필수입니다."),
    REFRESH_TOKEN_EXPIRED("440", "리프레시 토큰 만료"),
    INVALID_TOKEN("498", "유효하지 않은 토큰"),
    REFRESH_TOKEN_INVALID("499", "유효하지 않은 리프레시 토큰"),
    ERROR("500", "서버 오류"),
    REDIS_CONNECTION_FAILED("503", "Redis 연결에 실패했습니다.");

    private final String status;
    private final String message;

    ApiResponseType(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
