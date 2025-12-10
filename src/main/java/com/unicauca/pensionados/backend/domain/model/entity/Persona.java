package com.unicauca.pensionados.backend.domain.model.entity;

import java.time.LocalDate;


import java.util.ArrayList;

import java.util.List;

// Importar los nuevos enumeradores
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCivil;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.Genero;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "PERSONA")
@Inheritance(strategy =InheritanceType.JOINED) // problemas de mutabilidad
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
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

    @Column(name = "discapacidad", length = 255) // Campo para discapacidad (texto)
    private String discapacidad;

}