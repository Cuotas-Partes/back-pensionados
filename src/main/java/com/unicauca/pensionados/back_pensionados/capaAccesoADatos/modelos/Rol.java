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

    /**
     * Acciones (permisos) permitidas para este rol.
     * Se almacena en la tabla intermedia "rol_accion" como texto.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "rol_accion", joinColumns = @JoinColumn(name = "rol_id"))
    @Column(name = "accion")
    private Set<Accion> acciones = new HashSet<>();

    /** Setea timestamps automáticamente. */
    @PrePersist
    public void prePersist(){
        creadoEn = actualizadoEn = LocalDateTime.now(); }

    @PreUpdate
    public void preUpdate(){
        actualizadoEn = LocalDateTime.now(); }

    /**
     * Enum que define las acciones posibles del dominio.
     */
    public enum Accion {
        EJECUCION_PAGOS,       // Ejecutar pagos
        REGISTRO_PENSIONADO,   // Registrar pensionados
        PAGO_CUOTA_PARTE,      // Registrar pago de cuota parte
        GENERAR_REPORTE,       // Generación de reportes
        CONSULTAR_HISTORIAL    // Consultar historial de operaciones
    }
}

