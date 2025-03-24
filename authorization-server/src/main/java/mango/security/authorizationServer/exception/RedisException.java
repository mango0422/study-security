package mango.security.authorizationServer.exception;

import mango.security.authorizationServer.type.ApiResponseType;

public class RedisException extends RuntimeException {
    private final ApiResponseType responseType;

    public RedisException(ApiResponseType responseType, Throwable cause) {
        super(responseType.getMessage(), cause);
        this.responseType = responseType;
    }

    public ApiResponseType getResponseType() {
        return responseType;
    }
}
