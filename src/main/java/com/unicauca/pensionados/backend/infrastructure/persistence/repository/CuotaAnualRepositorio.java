package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CuotaAnualRepositorio extends JpaRepository<CuotaAnual, Long> {


    CuotaAnual findByAnio(Long anio);

}
