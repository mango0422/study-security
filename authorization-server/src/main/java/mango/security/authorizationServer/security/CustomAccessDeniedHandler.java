package mango.security.authorizationServer.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        BaseResponseDto<Object> errorResponse = BaseResponseDto.withMessage(ApiResponseType.FORBIDDEN, null);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(Integer.parseInt(ApiResponseType.FORBIDDEN.getStatus()));
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}