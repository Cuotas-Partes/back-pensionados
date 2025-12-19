package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoSustituto;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@PrimaryKeyJoinColumn (name = "idPersona")  //tiene la misma PK que Persona
@Table(name="sucesor")//tiene la misma PK que Persona
@Getter @Setter

public class Sucesor extends Persona{
    @Column(name="numero_documento", nullable = false, length = 50)
    private Long numeroDocumento;
    @Enumerated(EnumType.STRING) // Indica a JPA que guarde el nombre del enum como String
    @Column (name = "tipo_identificacion", nullable = false, length = 50)
    private TipoIdentificacion tipoDocumento;

    @Column(name="nombre_completo", nullable = false, length = 200)
    private String nombreCompleto;
    // Relación con el pensionado original
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "pensionado_sustituido_id")
    private Pensionado pensionadoSustituido;

    // La resolución que lo nombra sustituto
    @ManyToOne
    @JoinColumn(name = "resolucion_nombramiento_id")
    private Resolucion resolucionNombramiento;


    // Estado del sustituto
    @Enumerated(EnumType.STRING)
    private EstadoSustituto estado; // ACTIVO, INACTIVO, FALLECIDO



    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin; // null si está activo
    @Column(name = "porcentaje_pension", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajePension; // Ej: 50% si hay 2 sustitutos
}
