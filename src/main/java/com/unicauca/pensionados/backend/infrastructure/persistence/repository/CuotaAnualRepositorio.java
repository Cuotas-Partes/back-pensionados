package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaAnual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuotaAnualRepositorio extends JpaRepository<CuotaAnual, Long> {


    CuotaAnual findByAnio(Long anio);

}
