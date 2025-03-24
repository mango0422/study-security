package mango.security.authorizationServer.exception;

import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<>(BaseResponseDto.withMessage(ApiResponseType.ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAuthenticationException(AuthenticationException ex) {
        return new ResponseEntity<>(BaseResponseDto.withMessage(ApiResponseType.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(BaseResponseDto.withMessage(ApiResponseType.FORBIDDEN, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAuthException(AuthException ex) {
        return new ResponseEntity<>(BaseResponseDto.withMessage(ex.getResponseType(), ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleRedisException(RedisException ex) {
        return new ResponseEntity<>(
                BaseResponseDto.withMessage(ex.getResponseType(), ex.getMessage()),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }
}
