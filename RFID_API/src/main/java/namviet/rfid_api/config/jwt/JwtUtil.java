package namviet.rfid_api.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;

import namviet.rfid_api.config.data.CustomUserDetails;
import namviet.rfid_api.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final AccountService accountService;

    @Value("${jwt.secret-key}")
    private String secretket;

    public String generateToken(CustomUserDetails user){
        long expiration = 1000L * 60 * 60 * 24 * 30;
        var result  = accountService.findByUserName(user.getUsername());
        return JWT.create()
                .withSubject(user.getUsername())
                .withClaim("user_id",(Integer) result.getAccountId())
                .withClaim("role" ,(String) result.getRole().getRoleName())
                .withClaim("user_name",(String) result.getUserName())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(Algorithm.HMAC512(secretket));
    }

    public DecodedJWT verifyToken(String token){
        return JWT.require(Algorithm.HMAC512(secretket))
                .build()
                .verify(token);
    }

    public String extractUsername (String token){
        return verifyToken(token).getSubject();
    }

    public boolean isTokenExpried(String token){
        return verifyToken(token).getExpiresAt().before(new Date());
    }
}
