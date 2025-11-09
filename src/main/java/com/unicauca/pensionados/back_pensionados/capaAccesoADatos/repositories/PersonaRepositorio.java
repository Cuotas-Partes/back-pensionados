package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Persona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;

public interface PersonaRepositorio extends JpaRepository <Persona, Long>{

    // Spring Data JPA creará automáticamente la consulta para buscar por estos dos campos.
    boolean existsByTipoIdentificacionAndNumeroIdentificacion(TipoIdentificacion tipoIdentificacion, Long numeroIdentificacion);
}
