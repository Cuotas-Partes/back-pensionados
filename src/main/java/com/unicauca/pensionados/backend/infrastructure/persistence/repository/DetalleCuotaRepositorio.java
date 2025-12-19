package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.DetalleCuota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCuotaRepositorio extends JpaRepository<DetalleCuota, Long> {
}
