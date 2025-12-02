package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

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
@Table(name = "ROL")
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

    @ElementCollection(fetch = FetchType.EAGER, targetClass = Rol_Accion.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "rol_accion", joinColumns = @JoinColumn(name = "rol_id"))
    @Column(name = "accion")
    private List<Rol_Accion> acciones = new ArrayList<>();

}

