package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="SUCESOR")
@PrimaryKeyJoinColumn (name = "idPersona")  //tiene la misma PK que Persona
@Getter @Setter

public class Sucesor extends Persona{
    @Column (name = "fechaInicioSucesion", nullable = false)


    private LocalDate fechaInicioSucesion;

    @Column (name = "porcentajePension", nullable = false)
    private Double porcentajePension;


    @ManyToOne
    @JoinColumn(name = "numeroIdPensionado", nullable = false)
    @JsonBackReference("pensionado-sucesor")
    private Pensionado pensionado;
}
