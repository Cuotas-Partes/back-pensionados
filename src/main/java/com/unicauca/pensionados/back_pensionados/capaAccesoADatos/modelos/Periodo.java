package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import org.springdoc.core.converters.models.MonetaryAmount;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "PERIODO")
@Getter @Setter
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPeriodo;

    @ManyToOne
    @JoinColumn(name = "fechaIPC", nullable = false)
    private IPC IPC;

    @ManyToOne
    @JoinColumn(name = "idCuotaParte")
    private CuotaParte cuotaParte;

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

    //Declaramos atributos de tipo JavaMoney para poder realizar calculos mas precisos
    @Transient
    private MonetaryAmount valorPensionMoney;

    @Transient
    private MonetaryAmount cuotaParteMensualMoney;

    @Transient
    private MonetaryAmount cuotaParteTotalAnioMoney;

    @Transient
    private MonetaryAmount incrementoLey476Money;
    
}   