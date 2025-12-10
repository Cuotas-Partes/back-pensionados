package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.Persona;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;

public interface PersonaRepositorio extends JpaRepository <Persona, Long>{

    // Spring Data JPA creará automáticamente la consulta para buscar por estos dos campos.
    boolean existsByTipoIdentificacionAndNumeroIdentificacion(TipoIdentificacion tipoIdentificacion, Long numeroIdentificacion);
}
