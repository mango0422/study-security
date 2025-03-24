package mango.security.token_login.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mango.security.token_login.dto.BaseResponseDto;
import mango.security.token_login.type.ApiResponseType;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // 예외 발생 시, HTTP 500 에러와 함께 BaseResponseDto 형태의 JSON 응답을 반환
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json;charset=UTF-8");
            String message = "ExceptionHandlingFilter - Internal Error: " + ex.getMessage();
            BaseResponseDto<Object> errorResponse = new BaseResponseDto<>(ApiResponseType.ERROR, null);
            errorResponse.setMessage(message);
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}
