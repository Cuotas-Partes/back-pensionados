package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.LiquidacionEntidad;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoLiquidacion;

public interface LiquidacionEntidadRepositorio extends JpaRepository<LiquidacionEntidad, Long> {
    
    List<LiquidacionEntidad> findByEntidadNitEntidad(Long nitEntidad);
    
    List<LiquidacionEntidad> findByPeriodoIdPeriodo(Long periodoId);
    
    List<LiquidacionEntidad> findByEstado(EstadoLiquidacion estado);
    
    List<LiquidacionEntidad> findByPeriodoIdPeriodoAndEstado(Long periodoId, EstadoLiquidacion estado);
}
