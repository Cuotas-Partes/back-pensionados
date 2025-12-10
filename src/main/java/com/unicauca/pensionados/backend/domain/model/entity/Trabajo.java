package com.unicauca.pensionados.backend.domain.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "TRABAJO")
@Getter
@Setter
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTrabajo;

    @Column(name = "diasDeServicio", nullable = false)
    private Long diasDeServicio;

    
    @JsonBackReference
    @ManyToOne
    //@MapsId("idPersona")
    @JoinColumn(name = "idPersona", referencedColumnName = "idPersona")
    private Pensionado pensionado;

    @JsonBackReference(value = "entidad-trabajo")
    @ManyToOne
    //@MapsId("nitEntidad")
    @JoinColumn(name = "nitEntidad", referencedColumnName = "nitEntidad")
    private Entidad entidad;

}
