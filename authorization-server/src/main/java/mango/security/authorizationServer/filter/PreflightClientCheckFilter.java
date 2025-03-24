package mango.security.authorizationServer.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mango.security.authorizationServer.dto.BaseResponseDto;
import mango.security.authorizationServer.repository.RegisteredClientJpaRepository;
import mango.security.authorizationServer.type.ApiResponseType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class PreflightClientCheckFilter extends OncePerRequestFilter {
    private final RegisteredClientJpaRepository clientRepository;

    public PreflightClientCheckFilter(RegisteredClientJpaRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        if("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            String clientId = req.getHeader("client-id");
            if(clientId != null && !isClientIdRegistered(clientId)){
                BaseResponseDto<Object> responseDto = BaseResponseDto.withMessage(ApiResponseType.FORBIDDEN, null);
                res.setStatus(Integer.parseInt(ApiResponseType.FORBIDDEN.getStatus()));
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(new ObjectMapper().writeValueAsString(responseDto));
                return;
            }

        }
        filterChain.doFilter(req, res);
    }

    private boolean isClientIdRegistered(String clientId) {
        long count = clientRepository.countByClientId(clientId);
        return count > 0;
    }
}
