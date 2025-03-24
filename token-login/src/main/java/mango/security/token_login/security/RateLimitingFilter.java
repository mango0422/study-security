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
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitingFilter extends OncePerRequestFilter {

    // 1분 동안 최대 10회 요청 허용 (밀리초 단위)
    private static final long WINDOW_SIZE_MS = 60 * 1000;
    private static final int MAX_REQUESTS_PER_WINDOW = 10;

    // IP별 요청 횟수를 저장하는 ConcurrentHashMap
    private final Map<String, RateLimitInfo> ipRequestCounts = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String clientIp = request.getRemoteAddr();
        long currentTime = Instant.now().toEpochMilli();

        RateLimitInfo info = ipRequestCounts.get(clientIp);
        if (info == null || currentTime - info.windowStart > WINDOW_SIZE_MS) {
            info = new RateLimitInfo(currentTime, 0);
        }

        info.requestCount++;
        ipRequestCounts.put(clientIp, info);

        if (info.requestCount > MAX_REQUESTS_PER_WINDOW) {
            response.setStatus(429); // Too Many Requests
            response.setContentType("application/json;charset=UTF-8");
            String message = "RateLimitingFilter - Too Many Requests: 요청 횟수가 제한을 초과하였습니다. 잠시 후 다시 시도해 주세요.";
            BaseResponseDto<Object> errorResponse = new BaseResponseDto<>(ApiResponseType.ERROR, null);
            errorResponse.setMessage(message);
            objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static class RateLimitInfo {
        long windowStart;
        int requestCount;

        RateLimitInfo(long windowStart, int requestCount) {
            this.windowStart = windowStart;
            this.requestCount = requestCount;
        }
    }
}
