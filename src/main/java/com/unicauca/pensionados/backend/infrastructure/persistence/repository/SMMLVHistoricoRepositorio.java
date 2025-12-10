package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.SMMLVHistorico;

public interface SMMLVHistoricoRepositorio extends JpaRepository<SMMLVHistorico, Long> {
    
    Optional<SMMLVHistorico> findByAno(Integer ano);
}
