package mango.security.authorizationServer.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ApiResponseType responseType;
        // 토큰 만료 관련 예외 처리
        if (authException.getCause() != null &&
                authException.getCause().getMessage() != null &&
                authException.getCause().getMessage().toLowerCase().contains("expired")) {
            responseType = ApiResponseType.OUTDATED_TOKEN;
        } else {
            responseType = ApiResponseType.INVALID_TOKEN;
        }
        response.setCharacterEncoding("UTF-8");
        BaseResponseDto<Object> errorResponse = BaseResponseDto.withMessage(responseType, authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(Integer.parseInt(responseType.getStatus()));
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
