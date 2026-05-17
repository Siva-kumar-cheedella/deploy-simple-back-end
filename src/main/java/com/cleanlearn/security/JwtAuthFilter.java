package com.cleanlearn.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getServletPath();

        if (path.startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Processing JWT authentication for request: {}", request.getRequestURI());

        String token = parseJwt(request);

        try{

            if (token != null && jwtUtils.validateToken(token)) {
                String email = jwtUtils.getEmailFromToken(token);
                // UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                
                UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                    email, null, Collections.emptyList() // No authorities for simplicity
                );
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else {
                log.debug("No valid JWT token found in request");
                return;
            }
        }catch (Exception e) {
            log.error("Error during JWT authentication: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            
            response.getWriter().write(
                "{\"error\": \"Authentication failed\"}"
            );
            
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
