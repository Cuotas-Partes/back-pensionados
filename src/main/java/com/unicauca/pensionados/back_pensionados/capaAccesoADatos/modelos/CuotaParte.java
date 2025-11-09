package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.money.MonetaryAmount;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "CUOTA_PARTE")
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

    @OneToMany(mappedBy = "cuotaParte", cascade = CascadeType.ALL)
    private List<Periodo> periodos;
    
    //Declaramos atributos de tipo JavaMoney para poder realizar calculos mas precisos
    @Transient
    private MonetaryAmount valorCuotaParteMoney;
    
    @Transient
    private MonetaryAmount valorTotalCuotaParteMoney;
}