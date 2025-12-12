package com.unicauca.pensionados.backend.domain.model.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="sucesor")
@PrimaryKeyJoinColumn (name = "idPersona")  //tiene la misma PK que Persona
@Getter @Setter

public class Sucesor extends Persona{
    @Column (name = "fechaInicioSucesion", nullable = false)
    private LocalDate fechaInicioSucesion;

    @Column(name = "nombrePeriodo", length = 50)
    private String nombrePeriodo; // Ej: "2024-01", "2024-Q1"

    @Column(name = "numeroIdentificacion", nullable = false)
    private Long numeroIdentificacion; // Campo renombrado
    

    @Enumerated(EnumType.STRING) // Indica a JPA que guarde el nombre del enum como String
    @Column (name = "tipoIdentificacion", nullable = false, length = 50)
    private TipoIdentificacion tipoIdentificacion; // Campo renombrado y de tipo Enum

    @Column (name = "resolucion", nullable = false, length = 100)
    private String resolucion;
    
    @Column (name = "porcentajePension", nullable = false)
    private Double porcentajePension;


    @ManyToOne
    @JoinColumn(name = "numeroIdPensionado", nullable = false)
    @JsonBackReference("pensionado-sucesor")
    private Pensionado pensionado;
}
