package com.unicauca.pensionados.backend.domain.model.entity;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que representa a un usuario dentro del sistema.
 * Esta clase es una entidad de JPA que se mapea a la tabla "usuario"
 * y también implementa la interfaz UserDetails para integrarse con Spring Security.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="usuario", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Usuario implements UserDetails{
    
  // Identificador único (primary key) de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(nullable = false)
    String username;
    String password;
    String nombre;
    String apellido;

    @Column(name = "email", length = 100)
    String email;

    /** Rol asignado al usuario. */
    // La carga EAGER es importante aquí para que los permisos estén disponibles al autenticar
    @ManyToOne(fetch = FetchType.EAGER, optional = false) 
    @JoinColumn(name = "rol_id", nullable = false)
    //Muestra los datos del usuario y el rol
    @JsonBackReference
    private Rol rol;

    @Column(name = "estado", length = 50)
    @Builder.Default
    private String estado = "Activo"; // Activo, Inactivo, Suspendido

    @Column(name = "createdAt", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.rol.getAcciones() == null ?
            List.of() :
            this.rol.getAcciones().stream()
                    .map(accion -> new SimpleGrantedAuthority(accion.name()))
                    .collect(Collectors.toList());
    }

    /**
     * Indica si la cuenta no ha expirado.
     * Por ahora, siempre devuelve true. A futuro, se podría ligar a un campo en la BD.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     * Por ahora, siempre devuelve true. A futuro, se podría ligar a un campo en la BD.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales (contraseña) no han expirado.
     * Siempre devuelve true.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta está habilitada.
     * Verifica que el estado del usuario sea "Activo" (sin importar mayúsculas/minúsculas).
     */
    @Override
    public boolean isEnabled() {
        return "Activo".equalsIgnoreCase(estado);
    }
}
