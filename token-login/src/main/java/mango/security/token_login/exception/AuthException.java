package mango.security.token_login.exception;

import lombok.Getter;
import mango.security.token_login.type.ApiResponseType;

@Getter
public class AuthException extends RuntimeException {
    private final ApiResponseType responseType;

    public AuthException(ApiResponseType responseType, String message) {
        super(message);
        this.responseType = responseType;
    }
}
