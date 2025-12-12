package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPago;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPago")
    private Long idPago;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nit", referencedColumnName = "nit", nullable = false)
    private Entidad entidad;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPensionado", nullable = true)
    private Pensionado pensionado;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "valorPagado", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPagado;

    @Column(name = "capital", nullable = false, precision = 19, scale = 2)
    private BigDecimal capital;

    @Column(name = "saldo", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo; // Calculado: Capital - ValorPagado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipcInicialFecha", referencedColumnName = "fechaIPC")
    private IPC ipcInicial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ipcFinalFecha", referencedColumnName = "fechaIPC")
    private IPC ipcFinal;

    @Column(name = "valorIpcInicial", nullable = true, precision = 5, scale = 2)
    private BigDecimal valorIpcInicial;

    @Column(name = "valorIpcFinal", nullable = true, precision = 5, scale = 2)
    private BigDecimal valorIpcFinal;

    // Campos calculados para facilitar consultas
    @Transient
    public Long getNitEntidad() {
        return entidad != null ? Long.valueOf(entidad.getNit()) : null;
    }

    @Transient
    public Long getIdPensionado() {
        return pensionado != null ? pensionado.getIdPersona() : null;
    }

    @Transient
    public Integer getIpcInicialFecha() {
        return ipcInicial != null ? ipcInicial.getFechaIPC() : null;
    }

    @Transient
    public Integer getIpcFinalFecha() {
        return ipcFinal != null ? ipcFinal.getFechaIPC() : null;
    }

    @Column(name = "indexacion", nullable = true, precision = 19, scale = 2)
    private BigDecimal indexacion; // Calculado: (IPC Final / IPC Inicial) * Saldo

    @Column(name = "saldoPendiente", nullable = true, precision = 19, scale = 2)
    private BigDecimal saldoPendiente; // Calculado: Saldo + Indexación

    @Column(name = "fechaPago", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaPago;

    @Column(name = "verificado", nullable = false)
    private Boolean verificado = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoPago", nullable = false, length = 20)
    private TipoPago tipoPago;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

