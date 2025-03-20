package mango.security.basic_login.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;
import mango.security.basic_login.dto.BaseResponseDto;
import mango.security.basic_login.type.ApiResponseType;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                    AccessDeniedException accessDeniedException) throws IOException {
        BaseResponseDto<Object> errorResponse = new BaseResponseDto<>(ApiResponseType.FORBIDDEN, null);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(Integer.parseInt(ApiResponseType.FORBIDDEN.getStatus()));
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}