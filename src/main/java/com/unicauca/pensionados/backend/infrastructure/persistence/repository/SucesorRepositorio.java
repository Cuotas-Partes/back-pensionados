package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;

public interface SucesorRepositorio extends JpaRepository<Sucesor, Long> {
    boolean existsByTipoDocumentoAndNumeroDocumento(TipoIdentificacion tipoIdentificacion, Long numeroIdentificacion);
     
}
