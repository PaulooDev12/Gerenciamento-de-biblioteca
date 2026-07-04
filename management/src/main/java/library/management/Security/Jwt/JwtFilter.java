package library.management.Security.Jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.getJwtFromCookies(request);
        if (token != null) {
            Optional<JWTUserData> getUsr = jwtProvider.verifyToken(token);
            if (getUsr.isPresent()) {
                JWTUserData jwtUserData = getUsr.get();
                List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(jwtUserData.role()));
                UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(jwtUserData, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(userAuth);
            }
        }
        filterChain.doFilter(request, response);

    }
}

