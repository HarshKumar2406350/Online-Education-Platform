package com.onlineEducationPlatform.userModule.config;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import com.onlineEducationPlatform.userModule.service.Impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Value("${cookie.jwt.name}")
    private String jwtCookieName;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String jwt = extractJwtFromCookie(request);

        final String path = request.getServletPath();
    
        // Skip authentication for public endpoints
        if (path.startsWith("/api/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        
        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (jwtService.isTokenValid(jwt)) {
                String userEmail = jwtService.extractUsername(jwt);
                
                // Set authentication in context if not already authenticated
                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    String role = jwtService.extractRole(jwt);
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + role)
                    );
                    // Create authentication token

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities 
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Clear invalid token
            Cookie cookie = new Cookie(jwtCookieName, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(jwtCookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}