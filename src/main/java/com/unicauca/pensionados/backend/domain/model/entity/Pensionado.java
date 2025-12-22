package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoSustituto;
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
    @Column(name = "idPensionado")
    private Long idPensionado;

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
    private Long entityId;

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
    private BigDecimal valorPensionActual;

    // Referencia a la resolucion que establecio el valor actual
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolucion_vigente_id")
    private Resolucion resolucionVigente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private EstadoPersona estado = EstadoPersona.Activo;

    @Column(name = "fecha_fallecimiento")
    private LocalDate fechaFallecimiento;

    @OneToMany(mappedBy = "pensionadoSustituido", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sucesor> sustitutos;

    @Column(name = "tiene_sustituto")
    private Boolean tieneSustituto = false;

    @Column(name = "cuotas_pendientes")
    private Integer cuotasPendientes = 0;

    @Column(name = "total_pendiente", precision = 19, scale = 2)
    private BigDecimal totalPendiente = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pensionado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Resolucion> resoluciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.updatedAt = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        calcularPorcentajeCuota();
    }

    public void calcularPorcentajeCuota() {
        if (diasTotalesTrabajados != null && diasTotalesTrabajados > 0) {
            BigDecimal dias = new BigDecimal(diasTrabajadosEntidad);
            BigDecimal total = new BigDecimal(diasTotalesTrabajados);
            this.porcentajeCuota = dias
                    .divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.porcentajeCuota = BigDecimal.ZERO;
        }
    }
    // Método de negocio
    public List<Sucesor> getSustitutosActivos() {
        return sustitutos.stream()
                .filter(s -> s.getEstado() == EstadoSustituto.Activo)
                .toList();
    }
    public void actualizarValorPension(Resolucion nuevaResolucion) {
        if (nuevaResolucion.getTipoResolucion().modificaValor()) {
            this.valorPensionActual = nuevaResolucion.getValorResolucion();
            this.resolucionVigente = nuevaResolucion;
        }
    }

    // Validación de regla de negocio
    public boolean puedeAgregarSustituto() {
        return getSustitutosActivos().size() < 2;
    }

    // Calculos de negocio
}