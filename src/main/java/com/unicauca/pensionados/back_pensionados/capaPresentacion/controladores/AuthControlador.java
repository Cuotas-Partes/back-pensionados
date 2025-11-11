package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
//import com.unicauca.pensionados.back_pensionados.CapaServicio.seguridad.JwtTokenProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.unicauca.pensionados.back_pensionados.CapaServicio.seguridad.AuthService;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LoginPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.AuthRespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth Controlador", description = "Operaciones de autenticación de usuarios")
public class AuthControlador {

    private final AuthService authService;

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
    
    
}