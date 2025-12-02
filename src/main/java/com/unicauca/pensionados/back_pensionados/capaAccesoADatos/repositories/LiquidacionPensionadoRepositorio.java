package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.LiquidacionPensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoLiquidacion;

public interface LiquidacionPensionadoRepositorio extends JpaRepository<LiquidacionPensionado, Long> {
    
    List<LiquidacionPensionado> findByPensionadoIdPersona(Long pensionadoId);
    
    List<LiquidacionPensionado> findByPeriodoIdPeriodo(Long periodoId);
    
    List<LiquidacionPensionado> findByEstado(EstadoLiquidacion estado);
    
    List<LiquidacionPensionado> findByPeriodoIdPeriodoAndEstado(Long periodoId, EstadoLiquidacion estado);
}
