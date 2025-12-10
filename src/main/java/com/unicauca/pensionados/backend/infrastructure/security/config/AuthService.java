package com.unicauca.pensionados.backend.infrastructure.security.config;

import java.util.HashMap;
import java.util.Map;

import com.unicauca.pensionados.backend.infrastructure.security.jwt.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.unicauca.pensionados.backend.domain.model.entity.Rol;
import com.unicauca.pensionados.backend.domain.model.entity.Usuario;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.RolRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.UsuarioRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.LoginPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroPeticion;
import com.unicauca.pensionados.backend.application.dto.response.AuthRespuesta;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio; // <-- INYECTAR EL REPOSITORIO DE ROL
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthRespuesta login(LoginPeticion request){
        log.info("Intento de login para usuario: {}", request.getUsername());
        
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepositorio.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                log.error("Usuario no encontrado: {}", request.getUsername());
                return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrecta");
            });
        
        log.info("Usuario encontrado - Estado: {}, isEnabled: {}", usuario.getEstado(), usuario.isEnabled());
        
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            log.info("Autenticación exitosa para: {}", request.getUsername());
        }
        catch (DisabledException e) {
            log.error("Usuario deshabilitado: {}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario deshabilitado");
        }
        catch (BadCredentialsException e) {
            log.error("Credenciales incorrectas para: {}", request.getUsername());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrecta");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("rolId", usuario.getRol().getId());
        extraClaims.put("rolNombre", usuario.getRol().getNombre());
        String token = jwtService.getToken(usuario);
        return AuthRespuesta.builder()
            .token(token)
            .build();
    }

    public AuthRespuesta register(RegistroPeticion request){

        usuarioRepositorio.findByUsername(request.getUsername())
        .ifPresent(username ->{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"El correo ya esta registrado");
        });

        // CORRECCIÓN: Buscamos el Rol por su ID. Si no existe, lanzamos un error.
        Rol rolAsignado = rolRepositorio.findById(request.getIdRol())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "El rol especificado no existe"));

        Usuario usuario = Usuario.builder()
    
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .nombre(request.getNombre())
            .apellido(request.getApellido())
            .rol(rolAsignado) // <-- ASIGNAMOS EL OBJETO ROL COMPLETO
            .build();

            usuarioRepositorio.save(usuario);

            return AuthRespuesta.builder()
                .token(jwtService.getToken(usuario))
                .build();
    }

    
}
