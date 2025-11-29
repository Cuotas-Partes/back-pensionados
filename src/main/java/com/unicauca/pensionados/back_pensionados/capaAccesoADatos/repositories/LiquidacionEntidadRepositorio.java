package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.LiquidacionEntidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoLiquidacion;

public interface LiquidacionEntidadRepositorio extends JpaRepository<LiquidacionEntidad, Long> {
    
    List<LiquidacionEntidad> findByEntidadNitEntidad(Long nitEntidad);
    
    List<LiquidacionEntidad> findByPeriodoIdPeriodo(Long periodoId);
    
    List<LiquidacionEntidad> findByEstado(EstadoLiquidacion estado);
    
    List<LiquidacionEntidad> findByPeriodoIdPeriodoAndEstado(Long periodoId, EstadoLiquidacion estado);
}
