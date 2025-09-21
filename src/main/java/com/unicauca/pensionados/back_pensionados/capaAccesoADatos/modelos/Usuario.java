package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

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

    /** Rol asignado al usuario. */
    // La carga EAGER es importante aquí para que los permisos estén disponibles al autenticar
    @ManyToOne(fetch = FetchType.EAGER, optional = false) 
    @JoinColumn(name = "rol_id", nullable = false)
    //Muestra los datos del usuario y el rol
    @JsonBackReference
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        // Tomamos la lista de "Acciones" del objeto Rol
        // y las convertimos en "GrantedAuthority" que Spring Security entiende.
        return this.rol.getAcciones().stream()
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
     * Por ahora, siempre devuelve true. A futuro, se podría ligar a un campo en la BD.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
