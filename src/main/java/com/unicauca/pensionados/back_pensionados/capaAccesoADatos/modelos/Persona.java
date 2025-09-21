package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "PERSONA")
@Inheritance(strategy =InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Persona {
    @Id
    @Column(name = "numeroIdPersona")
    private Long numeroIdPersona;
    
    @Column (name =  "tipoIdPersona", nullable = false, length = 50)
    private String tipoIdPersona;

    @Column (name = "nombrePersona", nullable = false, length = 50)
    private String nombrePersona;

    @Column (name = "apellidosPersona", nullable = false, length = 50)
    private String apellidosPersona;

    @Column (name = "fechaNacimientoPersona", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaNacimientoPersona;

    @Column(name = "fechaExpedicionDocumentoIdPersona", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaExpedicionDocumentoIdPersona;

    @Column (name = "estadoPersona", nullable = false, length = 50)
    private String estadoPersona;

    @Column (name = "generoPersona", length = 50, nullable = true)
    private String generoPersona;

    @Column (name = "fechaDefuncionPersona", length = 50)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaDefuncionPersona;

}