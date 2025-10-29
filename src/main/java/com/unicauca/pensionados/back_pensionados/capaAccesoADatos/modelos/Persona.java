package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.time.LocalDate;


import java.util.ArrayList;

import java.util.List;

// Importar los nuevos enumeradores
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoCivil;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.Genero;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PERSONA",
       uniqueConstraints = { // Anotación para la unicidad de tipo y número de identificación
           @UniqueConstraint(columnNames = {"tipoIdentificacion", "numeroIdentificacion"})
       })
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona; // Un ID único para la persona, separado de su número de identificación

    @Column(name = "numeroIdentificacion", nullable = false)
    private Long numeroIdentificacion; // Campo renombrado
    

    @Enumerated(EnumType.STRING) // Indica a JPA que guarde el nombre del enum como String
    @Column (name = "tipoIdentificacion", nullable = false, length = 50)
    private TipoIdentificacion tipoIdentificacion; // Campo renombrado y de tipo Enum

    @Column (name = "nombrePersona", nullable = false, length = 50)
    private String nombrePersona;

    @Column (name = "apellidosPersona", nullable = false, length = 50)
    private String apellidosPersona;
    
    @Enumerated(EnumType.STRING)
    @Column (name = "estadoCivil", nullable = false, length = 50)
    private EstadoCivil estadoCivil; // Nuevo campo Estado Civil de tipo Enum

    @Column (name = "fechaNacimientoPersona", nullable = false)
    private LocalDate fechaNacimientoPersona;

    @Column(name = "fechaExpedicionDocumentoIdPersona", nullable = false)
    private LocalDate fechaExpedicionDocumentoIdPersona;


    @Enumerated(EnumType.STRING)
    @Column (name = "estadoPersona", nullable = false, length = 50)
    private EstadoPersona estadoPersona; // Campo cambiado a tipo Enum

    @Enumerated(EnumType.STRING)
    @Column (name = "generoPersona", length = 50)
    private Genero generoPersona; // Campo cambiado a tipo Enum

    @Column (name = "fechaDefuncionPersona")
    @Temporal(TemporalType.DATE)
    private LocalDate fechaDefuncionPersona;

    // Relación para las dependencias (trabajos)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "persona")
    private List<Trabajo> trabajos;


    //Una persona puede estar ligada a varios contratos
    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY)
    private List<Contrato> contratos = new ArrayList<>();

}