package com.unicauca.pensionados.backend.domain.model.entity;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPeriodo;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPeriodo;
import jakarta.persistence.EnumType;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springdoc.core.converters.models.MonetaryAmount;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreUpdate;

import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "periodo")
@Getter @Setter
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPeriodo;

    @Column(name = "nombrePeriodo", length = 50)
    private String nombrePeriodo; // Ej: "2024-01", "2024-Q1"

    @ManyToOne
    @JoinColumn(name = "fechaIPC", nullable = false)
    private IPC IPC;

    @ManyToOne
    @JoinColumn(name = "idCuotaParte")
    private CuotaParte cuotaParte;

    @ManyToOne
    @JoinColumn(name = "idSMMLV")
    private SMMLVHistorico smmlv;

    @Column(name = "fechaInicioPeriodo", nullable = false)
    private LocalDate fechaInicioPeriodo;

    @Column(name = "fechaFinPeriodo", nullable = false)
    private LocalDate fechaFinPeriodo;

    @Column (name = "numeroMesadas", nullable = false, precision = 5, scale = 2)
    private BigDecimal numeroMesadas;

    @Column (name = "valorPension", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPension;

    @Column (name = "cuotaParteMensual", nullable = false, precision = 19, scale = 2)
    private BigDecimal cuotaParteMensual;

    @Column (name = "cuotaParteTotalAnio", nullable = false, precision = 19, scale = 2)
    private BigDecimal cuotaParteTotalAnio;

    @Column (name = "incrementoLey476", nullable = true, precision = 19, scale = 2)
    private BigDecimal incrementoLey476;

    @Column(name = "resolucionPeriodo", length = 300)
    private String resolucionPeriodo;

    @Column(name = "estado", nullable = false, length = 50)
    @Enumerated(EnumType.STRING) // guarda el nombre del enum como texto
    // estado del periodo, no puede ser nulo
    private EstadoPeriodo estado;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoPeriodo") // Campo para control contable
    private EstadoPeriodo estadoPeriodo;

    @Column (name = "fechaExpedicion")
    private LocalDate fechaExpedicion;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    //Declaramos atributos de tipo JavaMoney para poder realizar calculos mas precisos
    @Transient
    private MonetaryAmount valorPensionMoney;

    @Transient
    private MonetaryAmount cuotaParteMensualMoney;

    @Transient
    private MonetaryAmount cuotaParteTotalAnioMoney;

    @Transient
    private MonetaryAmount incrementoLey476Money;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}   