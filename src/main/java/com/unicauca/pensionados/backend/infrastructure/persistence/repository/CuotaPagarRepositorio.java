package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaPagar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuotaPagarRepositorio extends JpaRepository<CuotaPagar, Long> {
    List<CuotaPagar> findByPensionado_IdPensionado(Long pensionadoId);
    List<CuotaPagar> findByEntidad_IdEntidad(Long entidadId);
}
