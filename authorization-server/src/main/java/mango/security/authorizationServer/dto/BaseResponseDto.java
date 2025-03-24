package mango.security.authorizationServer.dto;

import mango.security.authorizationServer.type.ApiResponseType;

public record BaseResponseDto<T>(String status, String message, T data) {
    public static <T> BaseResponseDto<T> of(ApiResponseType type, T data) {
        return new BaseResponseDto<>(type.getStatus(), type.getMessage(), data);
    }

    public static <T> BaseResponseDto<T> withMessage(ApiResponseType type, String message) {
        return new BaseResponseDto<>(type.getStatus(), message, null);
    }
}
