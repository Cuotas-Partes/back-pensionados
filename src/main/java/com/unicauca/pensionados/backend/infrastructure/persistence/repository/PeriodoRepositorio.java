package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeriodoRepositorio extends JpaRepository<Periodo, Long> {
    Optional<Periodo> findByAnio(Integer anio);
}
