package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.money.MonetaryAmount;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuota;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "cuotaParte")
@Getter @Setter
public class CuotaParte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuotaParte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTrabajo", nullable = false)
    private Trabajo trabajo;

    @Column (name = "valorCuotaParte", nullable = false,precision = 19, scale = 0 )
    private BigDecimal valorCuotaParte;

    @Column (name = "porcentajeCuotaParte", nullable = false, precision = 5, scale = 4)
    private BigDecimal porcentajeCuotaParte;

    @Column(name = "fechaGeneracion", nullable = true)
    private LocalDate fechaGeneracion;

    @Column (name = "notas", nullable = true, length = 200)
    private String notas;

    @Column (name = "cuotaParteTotal", nullable = true, precision = 19, scale = 0)
    private BigDecimal valorTotalCuotaParte;	

    // Campos adicionales según schema
    @Column(name = "valorCorriente", precision = 19, scale = 2)
    private BigDecimal valorCorriente = BigDecimal.ZERO;

    @Column(name = "valorNoCorriente", precision = 19, scale = 2)
    private BigDecimal valorNoCorriente = BigDecimal.ZERO;

    @Column(name = "valorPrescrito", precision = 19, scale = 2)
    private BigDecimal valorPrescrito = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private EstadoCuota estado = EstadoCuota.PENDIENTE;

    @Column(name = "fechaVencimiento")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaVencimiento;

    @Column(name = "fechaPago")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaPago;

    @Column(name = "fechaActualizacion")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaActualizacion;

    @Column(name = "usuarioActualizacion", length = 100)
    private String usuarioActualizacion;

    @Column(name = "observaciones", length = 250)
    private String observaciones;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cuotaParte", cascade = CascadeType.ALL)
    private List<Periodo> periodos;
    
    //Declaramos atributos de tipo JavaMoney para poder realizar calculos mas precisos
    @Transient
    private MonetaryAmount valorCuotaParteMoney;
    
    @Transient
    private MonetaryAmount valorTotalCuotaParteMoney;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}