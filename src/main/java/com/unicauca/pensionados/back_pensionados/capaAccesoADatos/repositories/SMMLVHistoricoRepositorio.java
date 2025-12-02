package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.SMMLVHistorico;

public interface SMMLVHistoricoRepositorio extends JpaRepository<SMMLVHistorico, Long> {
    
    Optional<SMMLVHistorico> findByAno(Integer ano);
}
