package com.unicauca.pensionados.back_pensionados.CapaServicio.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@OpenAPIDefinition(
    info = @Info(title = "Cuotas Partes API", version = "v1", description = "API para la gestión de Cuotas Partes y Pensionados"),
    security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final CustomAuthenticationEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Value("${CORS_URL:https://front-pensionados-production.up.railway.app/}")
    private String corsUrl;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                  .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(customAuthEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler))
                .authorizeHttpRequests(authRequest -> authRequest
                    .requestMatchers(  "/auth/**",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/swagger-ui/index.html/**",    // Por si acaso
                    "/swagger-resources/**",
                    "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager -> 
                    sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
        }


        // configuracion CORS
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            // configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173",corsUrlFront,corsUrlDok));
            // configuration.setAllowedOrigins(Arrays.asList(corsUrl.split(",")));
            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173","http://54.80.246.88:3000", "http://54.80.246.88:5173", "https://front-pensionados-production.up.railway.app/"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS" , "PATCH"));
            configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
}

