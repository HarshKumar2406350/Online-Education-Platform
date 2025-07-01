package com.onlineEducationPlatform.assignmentAndQuiz.config;

import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${cookie.jwt.name}")
    private String jwtCookieName;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.debug("===== JWT Filter Start =====");
        log.debug("Request URI: {}", request.getRequestURI());
        log.debug("Request Method: {}", request.getMethod());
        
        final String jwt = extractJwtFromCookie(request);
        log.debug("JWT Token Status: {}", jwt != null ? "Found" : "Not Found");
        
        if (jwt == null) {
            log.debug("No JWT token found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }
    
        // Inside doFilterInternal method:
try {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(secretKey.getBytes())
        .build()
        .parseClaimsJws(jwt)
        .getBody();

    String userEmail = claims.getSubject();
    String role = claims.get("role", String.class);
    log.debug("JWT Claims - Email: {}, Role: {}", userEmail, role);

    // Handle multiple roles if present
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
    if (role.contains(",")) {
        Arrays.stream(role.split(","))
            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r.trim())
            .map(SimpleGrantedAuthority::new)
            .forEach(authorities::add);
    } else {
        String formattedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        authorities.add(new SimpleGrantedAuthority(formattedRole));
    }

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        userEmail,
        null,
        authorities
    );
    
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authToken);
    log.debug("Authentication token set in SecurityContext with authorities: {}", 
        authToken.getAuthorities());
} catch (Exception e) {
    log.error("JWT Token validation failed", e);
    SecurityContextHolder.clearContext();
}
    
        log.debug("===== JWT Filter End =====");
        filterChain.doFilter(request, response);
    }
    
    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        log.debug("Extracting JWT from cookies");
        log.debug("Number of cookies: {}", cookies != null ? cookies.length : 0);
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.debug("Cookie found - Name: {}", cookie.getName());
                if (jwtCookieName.equals(cookie.getName())) {
                    log.debug("JWT cookie found");
                    return cookie.getValue();
                }
            }
        }
        log.debug("JWT cookie not found");
        return null;
    }   

}