package com.unicauca.pensionados.backend.domain.model.entity;

import com.unicauca.pensionados.backend.domain.model.enums.RolAccion;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * Entidad que representa un Rol dentro del sistema de seguridad.
 * Un Rol puede estar asociado a muchos usuarios y tener un conjunto de acciones (permisos) permitidas.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rol")
public class Rol {

    /** Identificador único del rol (PK autoincremental). */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del rol (ej.: "administrador"). Único. */
    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

       /** Indica si el rol está activo (no se borra físicamente). */
    @Column(nullable = false)
    private Boolean activo = true;

    /** Timestamps de creación y última actualización. */
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
    /**
     * Un rol puede tener muchos usuarios.
     */
    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
    //Evita la Referencia Ciclica
    @JsonManagedReference
    private List<Usuario> usuarios = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER, targetClass = RolAccion.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "ROL_ACCION", joinColumns = @JoinColumn(name = "rol_id"))
    @Column(name = "accion")
    private List<RolAccion> acciones = new ArrayList<>();

}

