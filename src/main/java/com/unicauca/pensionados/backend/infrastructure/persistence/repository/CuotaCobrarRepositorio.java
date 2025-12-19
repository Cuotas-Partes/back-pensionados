package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaCobrar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuotaCobrarRepositorio extends JpaRepository<CuotaCobrar, Long> {
    List<CuotaCobrar> findByPensionado_IdPersona(Long pensionadoId);
    List<CuotaCobrar> findByEntidad_IdEntidad(Long entidadId);
}
