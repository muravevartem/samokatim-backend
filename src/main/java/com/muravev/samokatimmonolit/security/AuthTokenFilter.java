package com.muravev.samokatimmonolit.security;

import com.muravev.samokatimmonolit.error.ApiException;
import com.muravev.samokatimmonolit.error.StatusCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    @Value("${auth.secret}")
    private String secret;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> tokenRequest = getTokenRequest(request);
            if (tokenRequest.isPresent()) {
                String token = tokenRequest.get();
                log.info("Token : {}", token);
                JwtParser parser = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                        .build();
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                Claims body = claimsJws.getBody();
                String username = body.get("username", String.class);
                List<String> roles = body.get("roles", List.class).stream()
                        .map(Object::toString)
                        .toList();

                List<? extends GrantedAuthority> authorities = roles.stream()
                        .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                        .toList();

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
        } catch (Exception e) {
            throw new ApiException(StatusCode.FORBIDDEN);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            return Optional.of(authorization.substring(7));
        }
        return Optional.empty();
    }
}
