package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Eventos.HistoricoLiquidacionPorCobrar;
import jakarta.persistence.*;
import org.springdoc.core.converters.models.MonetaryAmount;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.Estado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoPension;

@Entity
@Table (name ="PENSIONADO")
@PrimaryKeyJoinColumn (name = "idPersona") //tiene la misma PK que Persona
@Setter @Getter
public class Pensionado extends Persona{
    @Column (name = "fechaInicioPension", nullable = true)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaInicioPension;

    @Column (name = "fechaIngreso", nullable = true)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaIngreso;
    
    @Column (name = "valorInicialPension", nullable = false, precision = 19, scale = 0)
    private BigDecimal valorInicialPension;

    @Column (name = "resolucionPension", nullable =false , length = 50)
    private String resolucionPension;
    
    @Column (name = "totalDiasTrabajo", nullable = true)
    private Long totalDiasTrabajo;

    @Column (name = "aplicarIPCPrimerPeriodo", nullable = false)
    private boolean aplicarIPCPrimerPeriodo = false;

    //relacion entidad de Jubilacion
    @JsonBackReference //rompe el ciclo infinito de serializacion al mostrar el JSON
    @ManyToOne
    @JoinColumn(name = "nitEntidad", nullable = false)
    private Entidad entidadJubilacion; 

    @Column(name = "tipoPension", nullable = false, length = 50)
    @Enumerated(EnumType.STRING) // guarda el nombre del enum como texto
    // tipo de pension o jubilacion, no puede ser nulo
    private TipoPension tipoPension;

    //relacion 1 a muchos con trabajo
    @JsonManagedReference
    @OneToMany (mappedBy = "pensionado", cascade = CascadeType.ALL)
    private List <Trabajo> trabajos;

    //Relacion con HistoricoLiquidacionPorCobrar
    @OneToMany(mappedBy = "pensionado", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<HistoricoLiquidacionPorCobrar> historicos;

    @JsonManagedReference("pensionado-sucesor")
    @OneToMany(mappedBy = "pensionado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sucesor> sucesores;

    //Declaramos atributos de tipo JavaMoney para poder realizar calculos mas precisos
    @Transient 
    private MonetaryAmount valorInicialPensionMoney;

}