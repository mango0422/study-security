package mango.security.authorizationServer.exception;

import mango.security.authorizationServer.type.ApiResponseType;

public class AuthException extends RuntimeException {
    private final ApiResponseType responseType;

    public AuthException(ApiResponseType responseType, String message) {
        super(message);
        this.responseType = responseType;
    }

    public ApiResponseType getResponseType() {
        return responseType;
    }
}
