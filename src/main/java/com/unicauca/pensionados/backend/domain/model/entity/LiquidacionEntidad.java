package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoLiquidacion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "liquidacionEntidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionEntidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nit", nullable = false)
    private Entidad entidad;

    @Column(name = "entityNit", nullable = false)
    private String entityNit;

    @Column(name = "entityNombre", nullable = false, length = 200)
    private String entityNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPeriodo", nullable = false)
    private Periodo periodo;

    @Column(name = "periodoNombre", length = 100)
    private String periodoNombre;

    @Column(name = "totalACobrar", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalACobrar;

    @Column(name = "valorCorriente", precision = 19, scale = 2)
    private BigDecimal valorCorriente = BigDecimal.ZERO;

    @Column(name = "valorNoCorriente", precision = 19, scale = 2)
    private BigDecimal valorNoCorriente = BigDecimal.ZERO;

    @Column(name = "valorPrescrito", precision = 19, scale = 2)
    private BigDecimal valorPrescrito = BigDecimal.ZERO;

    @Column(name = "descuentos", precision = 19, scale = 2)
    private BigDecimal descuentos = BigDecimal.ZERO;

    @Column(name = "neto", precision = 19, scale = 2)
    private BigDecimal neto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoLiquidacion estado = EstadoLiquidacion.PENDIENTE;

    @Column(name = "fechaLiquidacion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaLiquidacion;

    @Column(name = "fechaCobro")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaCobro;

    @Column(name = "pensionadosCount")
    private Integer pensionadosCount = 0;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles; // JSON con detalles por pensionado

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
