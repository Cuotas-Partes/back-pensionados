package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Reporte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoReporte;

public interface ReporteRepositorio extends JpaRepository<Reporte, Long> {
    
    List<Reporte> findByTipo(TipoReporte tipo);
    
    List<Reporte> findByUsuarioId(Integer usuarioId);
    
    List<Reporte> findByTipoAndUsuarioId(TipoReporte tipo, Integer usuarioId);
}
