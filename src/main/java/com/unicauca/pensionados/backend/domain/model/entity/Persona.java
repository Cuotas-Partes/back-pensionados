package com.unicauca.pensionados.backend.domain.model.entity;

import java.time.LocalDate;


import java.util.ArrayList;

import java.util.List;

// Importar los nuevos enumeradores
import com.unicauca.pensionados.backend.domain.model.enums.*;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "persona")
@Inheritance(strategy =InheritanceType.JOINED) // problemas de mutabilidad
// @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter @Setter @NoArgsConstructor
public abstract class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPersona; // Un ID único para la persona, separado de su número de identificación

}