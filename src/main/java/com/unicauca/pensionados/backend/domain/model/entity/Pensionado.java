package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPension;

@Entity
@Table(name = "pensionado")
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@AllArgsConstructor
@NoArgsConstructor
public class Pensionado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPersona")
    private Long idPersona;

    @Column(name = "cedula", nullable = false, unique = true, length = 20)
    private String cedula;

    @Column(name = "fecha_expedicion_cedula", nullable = false)
    private LocalDate fechaExpedicionCedula;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "apellidos", nullable = false, length = 200)
    private String apellidos;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "correo", length = 100)
    private String correo;

    @Column(name = "entidad_jubilacion", nullable = false, length = 200)
    private String entidadJubilacion;

    @Column(name = "entity_nit", nullable = false, length = 20)
    private String entityNit;

    @Column(name = "entity_id", nullable = false, length = 20)
    private String entityId;

    @Column(name = "dias_trabajados_entidad", nullable = false)
    private Integer diasTrabajadosEntidad;

    @Column(name = "dias_totales_trabajados", nullable = false)
    private Integer diasTotalesTrabajados;

    @Column(name = "porcentaje_cuota", nullable = false, precision = 19, scale = 2)
    private BigDecimal porcentajeCuota;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_jubilacion", nullable = false, length = 50)
    private TipoPension tipoJubilacion;

    @Column(name = "valor_pension", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPension;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private EstadoPersona estado = EstadoPersona.Activo;

    @Column(name = "fecha_fallecimiento")
    private LocalDate fechaFallecimiento;

    @Column(name = "pensionado_sustituido", length = 200)
    private String pensionadoSustituido;

    @Column(name = "tiene_sustituto")
    private Boolean tieneSustituto = false;

    @Column(name = "sustituto_id")
    private Long sustitutoId;

    @Column(name = "cuotas_pendientes")
    private Integer cuotasPendientes = 0;

    @Column(name = "total_pendiente", precision = 19, scale = 2)
    private BigDecimal totalPendiente = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pensionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Resolucion> resoluciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}