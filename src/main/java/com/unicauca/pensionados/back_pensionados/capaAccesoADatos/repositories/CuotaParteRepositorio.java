package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;



public interface CuotaParteRepositorio extends JpaRepository <CuotaParte, Long> {
    Optional<CuotaParte> findByTrabajoIdTrabajo(Long idTrabajo);
    List<CuotaParte> findByFechaGeneracionBetween(LocalDate fechaInicio, LocalDate fechaFin);

  @Query("""
    SELECT DISTINCT cp
    FROM CuotaParte cp
    JOIN FETCH cp.periodos p
    WHERE p.fechaInicioPeriodo <= :fechaFin
      AND p.fechaFinPeriodo >= :fechaInicio
    """)
    List<CuotaParte> findCuotasParteByPeriodoEnRango(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );


}