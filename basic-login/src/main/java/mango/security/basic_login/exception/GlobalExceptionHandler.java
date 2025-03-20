package mango.security.basic_login.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import mango.security.basic_login.dto.BaseResponseDto;
import mango.security.basic_login.type.ApiResponseType;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAllExceptions(Exception ex) {
        // 예외 발생 시 ApiResponseType.ERROR를 사용하고, 메시지를 세팅
        BaseResponseDto<Object> response = new BaseResponseDto<>(ApiResponseType.ERROR, null);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAuthenticationException(AuthenticationException ex) {
        // 인증 예외 발생 시 ApiResponseType.UNAUTHORIZED를 사용하고, 메시지를 세팅
        BaseResponseDto<Object> response = new BaseResponseDto<>(ApiResponseType.UNAUTHORIZED, null);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        // 접근 권한 예외 발생 시 ApiResponseType.FORBIDDEN를 사용하고, 메시지를 세팅
        BaseResponseDto<Object> response = new BaseResponseDto<>(ApiResponseType.FORBIDDEN, null);
        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}