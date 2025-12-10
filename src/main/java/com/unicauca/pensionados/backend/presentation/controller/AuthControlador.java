package com.unicauca.pensionados.backend.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
//import com.unicauca.pensionados.backend.infrastructure.security.jwtTokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.unicauca.pensionados.backend.infrastructure.security.config.AuthService;
import com.unicauca.pensionados.backend.application.dto.request.LoginPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroPeticion;
import com.unicauca.pensionados.backend.application.dto.response.AuthRespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controlador", description = "Operaciones de autenticación de usuarios")
@CrossOrigin(origins = "*")
public class AuthControlador {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "Iniciar sesión", description = "Endpoint para autenticar a un usuario y generar el token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthRespuesta> login(@RequestBody LoginPeticion request) 
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Registrar usuario", description = "Registra un nuevo usuario en el sistema y retorna su token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Registro exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthRespuesta> register(@RequestBody RegistroPeticion request) {
        
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
    
    // Endpoint temporal para generar hash de contraseña
    @GetMapping("/generate-hash")
    public ResponseEntity<String> generateHash(@RequestParam String password) {
        String hash = passwordEncoder.encode(password);
        return ResponseEntity.ok("Hash para '" + password + "': " + hash);
    }

}
