package mango.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/messages")
    public Map<String, Object> getMessages(@AuthenticationPrincipal Jwt jwt) {
        // @AuthenticationPrincipal Jwt injects the validated JWT details
        return Collections.singletonMap("message", "Hello " + jwt.getSubject() + ", you have access!");
    }
}