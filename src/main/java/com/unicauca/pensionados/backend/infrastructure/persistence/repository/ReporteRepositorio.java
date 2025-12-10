package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.Reporte;
import com.unicauca.pensionados.backend.domain.model.enums.TipoReporte;

public interface ReporteRepositorio extends JpaRepository<Reporte, Long> {
    
    List<Reporte> findByTipo(TipoReporte tipo);
    
    List<Reporte> findByUsuarioId(Integer usuarioId);
    
    List<Reporte> findByTipoAndUsuarioId(TipoReporte tipo, Integer usuarioId);
}
