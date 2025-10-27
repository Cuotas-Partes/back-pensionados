package com.unicauca.pensionados.back_pensionados.CapaServicio.seguridad;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UserDetails;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Rol;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Usuario;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.UsuarioRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LoginPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.AuthRespuesta;

import lombok.RequiredArgsConstructor;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.RolRepositorio;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio; // <-- INYECTAR EL REPOSITORIO DE ROL
    private final JwtService jwtService; 
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthRespuesta login(LoginPeticion request){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }
        catch (BadCredentialsException e) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrecta");
        }
        
        UserDetails usuario= usuarioRepositorio.findByUsername(request.getUsername()).orElseThrow();
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
