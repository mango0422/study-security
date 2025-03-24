package mango.security.token_login.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

public abstract class AbstractJwtTokenProvider {

    @Value("${jwt.accessToken.ttl}")
    protected long expireTime;

    // 각 구현체에서 secret key를 기반으로 Key 객체를 생성하여 리턴
    protected abstract Key getKey();

    public String generateToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireTime);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
