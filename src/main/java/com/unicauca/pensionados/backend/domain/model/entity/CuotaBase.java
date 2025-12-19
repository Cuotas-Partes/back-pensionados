package com.unicauca.pensionados.backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class CuotaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pensionado_id", nullable = false)
    private Pensionado pensionado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entidad_id", nullable = false)
    private Entidad entidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private Periodo periodo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_liquidacion", nullable = false)
    private LocalDate fechaLiquidacion;

    @Column(name = "dias_totales", nullable = false)
    private Integer diasTotales;

    @Column(name = "cantidad_cuotas", nullable = false)
    private Integer cantidadCuotas;

    @Column(name = "valor_cuota_parte", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorCuotaParte;

    @Column(name = "valor_cuotas_total", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorCuotasTotal;


    @Column(name = "ajuste", precision = 19, scale = 2)
    private BigDecimal ajuste;

    @Column(name = "es_reliquidacion")
    private Boolean esReliquidacion;

    @Column(name = "comentarios", columnDefinition = "TEXT")
    private String comentarios;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (esReliquidacion == null) esReliquidacion = false;
        if (ajuste == null) ajuste = BigDecimal.ZERO;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
