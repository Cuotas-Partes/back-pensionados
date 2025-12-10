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
@Table(name = "LIQUIDACION_PENSIONADO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LiquidacionPensionado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPensionado", nullable = false)
    private Pensionado pensionado;

    @Column(name = "pensionadoCedula", nullable = false)
    private String pensionadoCedula;

    @Column(name = "pensionadoNombre", nullable = false, length = 200)
    private String pensionadoNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPeriodo", nullable = false)
    private Periodo periodo;

    @Column(name = "periodoNombre", length = 100)
    private String periodoNombre;

    @Column(name = "totalAPagar", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAPagar;

    @Column(name = "valorCorriente", precision = 19, scale = 2)
    private BigDecimal valorCorriente = BigDecimal.ZERO;

    @Column(name = "valorNoCorriente", precision = 19, scale = 2)
    private BigDecimal valorNoCorriente = BigDecimal.ZERO;

    @Column(name = "valorPrescrito", precision = 19, scale = 2)
    private BigDecimal valorPrescrito = BigDecimal.ZERO;

    @Column(name = "descuentos", precision = 19, scale = 2)
    private BigDecimal descuentos = BigDecimal.ZERO;

    @Column(name = "netoPagable", precision = 19, scale = 2)
    private BigDecimal netoPagable;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoLiquidacion estado = EstadoLiquidacion.PENDIENTE;

    @Column(name = "fechaLiquidacion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaLiquidacion;

    @Column(name = "fechaPago")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaPago;

    @Column(name = "detalles", columnDefinition = "TEXT")
    private String detalles; // JSON con detalles de la liquidación

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
