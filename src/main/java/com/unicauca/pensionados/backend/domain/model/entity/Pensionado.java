package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPension;

@Entity
@Table(name = "pensionados")
@Setter
@Getter
public class Pensionado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPersona")
    private Long idPersona;

    @Column(name = "cedula", nullable = false, unique = true, columnDefinition = "TEXT")
    private String cedula;

    @Column(name = "fecha_expedicion_cedula", nullable = false)
    private LocalDate fechaExpedicionCedula;

    @Column(name = "nombre", nullable = false, columnDefinition = "TEXT")
    private String nombre;

    @Column(name = "apellidos", nullable = false, columnDefinition = "TEXT")
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "telefono", nullable = false, columnDefinition = "TEXT")
    private String telefono;

    @Column(name = "correo", columnDefinition = "TEXT")
    private String correo;

    @Column(name = "entidad_jubilacion", nullable = false, columnDefinition = "TEXT")
    private String entidadJubilacion;

    @Column(name = "entity_nit", nullable = false, columnDefinition = "TEXT")
    private String entityNit;

    @Column(name = "entity_id", nullable = false, columnDefinition = "TEXT")
    private String entityId;

    @Column(name = "dias_trabajados_entidad", nullable = false)
    private Integer diasTrabajadosEntidad;

    @Column(name = "dias_totales_trabajados", nullable = false)
    private Integer diasTotalesTrabajados;

    @Column(name = "porcentaje_cuota", nullable = false, precision = 19, scale = 2)
    private BigDecimal porcentajeCuota;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_jubilacion", nullable = false, columnDefinition = "TEXT")
    private TipoPension tipoJubilacion;

    @Column(name = "valor_pension", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPension;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "TEXT DEFAULT 'Activo'")
    private EstadoPersona estado = EstadoPersona.ACTIVO;

    @Column(name = "fecha_fallecimiento")
    private LocalDate fechaFallecimiento;

    @Column(name = "pensionado_sustituido", columnDefinition = "TEXT")
    private String pensionadoSustituido;

    @Column(name = "tiene_sustituto")
    private Boolean tieneSustituto = false;

    @Column(name = "sustituto_id")
    private Long sustitutoId;

    @Column(name = "cuotas_pendientes")
    private Integer cuotasPendientes = 0;

    @Column(name = "total_pendiente", precision = 19, scale = 2)
    private BigDecimal totalPendiente = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}