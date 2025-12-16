package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.DTF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DTFRepositorio extends JpaRepository<DTF, Long> {

    Optional<DTF> findByPeriodo(String periodo);
    List<DTF> findByActivoTrue();
    Optional<DTF> findByIdAndActivoTrue(Long id);


}
