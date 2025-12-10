package com.unicauca.pensionados.backend.infrastructure.security.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import org.springframework.http.HttpHeaders;



@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String path = request.getServletPath();
        log.debug("Procesando request: {} {}", request.getMethod(), path);

        String token = getTokenFromRequest(request);

        if (token == null) {
            log.debug("No se encontró token en el request");
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("Token encontrado, validando...");

        String username = null;

        try {
            username = jwtService.getUsernameFromToken(token);
            log.debug("Username extraído del token: {}", username);
        } catch (Exception e) {
            log.error("Error al extraer username del token: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.debug("Usuario cargado - isEnabled: {}, authorities: {}", 
                userDetails.isEnabled(), userDetails.getAuthorities());

            if (jwtService.isTokenValid(token, userDetails)) {
                log.debug("Token válido, estableciendo autenticación");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Autenticación establecida exitosamente");
            } else {
                log.warn("Token inválido para usuario: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }



    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // no hay header -> no filtrar
        if (!StringUtils.hasText(authHeader)) {
            return null;
        }

        // no empieza por "Bearer " -> no filtrar
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }

        // extraer token
        String token = authHeader.substring(7);

        // token vacío -> no filtrar
        if (!StringUtils.hasText(token)) {
            return null;
        }

        return token;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth/login") || path.startsWith("/auth/register")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");
    }


    
}

