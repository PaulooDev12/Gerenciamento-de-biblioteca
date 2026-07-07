package library.management.Security.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import library.management.Entites.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Optional;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.cookie.name}")
    private String jwtCookieName;

    public String generateJwtToken(Usuario usuario) {
        String roleName = usuario.getRole().name();
        if(!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }
        return JWT.create()
                .withClaim("id", usuario.getId())
                .withSubject(usuario.getUsername())
                .withClaim("role", roleName)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(expiration))
                .sign(Algorithm.HMAC256(secret));
    }
    public Optional<JWTUserData> verifyToken(String token) {

        try{
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
            return Optional.of(JWTUserData.builder()
                    .id(decodedJWT.getClaim("id").asLong())
                    .username(decodedJWT.getSubject())
                    .role(decodedJWT.getClaim("role").asString())
                    .build()
            );
        }catch (JWTVerificationException e){
            return Optional.empty();
        }

    }
    public ResponseCookie generateJwtCookie(Usuario usuario) {
        String token = generateJwtToken(usuario);
        return ResponseCookie.from(jwtCookieName, token)
                .path("/")
                .httpOnly(true)
                .maxAge(expiration)
                .secure(false) // é um somente um teste em produção o secure é true para permitir apenas https
                .sameSite("Strict")
                .build();

    }
    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookieName, "")
                .path("/")
                .maxAge(0)
                .build();
    }
}

