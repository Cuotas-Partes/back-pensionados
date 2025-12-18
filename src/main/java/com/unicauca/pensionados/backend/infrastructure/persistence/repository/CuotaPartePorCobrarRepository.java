package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaPartePorCobrar;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaParte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para gestionar las cuotas partes por cobrar
 */
@Repository
public interface CuotaPartePorCobrarRepository extends JpaRepository<CuotaPartePorCobrar, Long> {

    /**
     * Buscar cuotas partes por pensionado
     */
    List<CuotaPartePorCobrar> findByPensionado_IdPersona(Long idPensionado);

    /**
     * Buscar cuotas partes por entidad deudora
     */
    List<CuotaPartePorCobrar> findByEntidadDeudora_IdEntidad(Long idEntidad);

    /**
     * Buscar cuotas partes por periodo (año y mes)
     */
    List<CuotaPartePorCobrar> findByAnioPeriodoAndMesPeriodo(Integer anio, Integer mes);

    /**
     * Buscar cuotas partes por año
     */
    List<CuotaPartePorCobrar> findByAnioPeriodo(Integer anio);

    /**
     * Buscar cuotas partes por estado
     */
    List<CuotaPartePorCobrar> findByEstado(EstadoCuotaParte estado);

    /**
     * Buscar cuotas partes pendientes por entidad deudora
     */
    List<CuotaPartePorCobrar> findByEntidadDeudora_IdEntidadAndEstado(Long idEntidad, EstadoCuotaParte estado);

    /**
     * Buscar cuotas partes por pensionado y periodo
     */
    List<CuotaPartePorCobrar> findByPensionado_IdPersonaAndAnioPeriodoAndMesPeriodo(
        Long idPensionado, Integer anio, Integer mes
    );

    /**
     * Buscar cuotas partes por rango de fechas
     */
    @Query("SELECT c FROM CuotaPartePorCobrar c WHERE c.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin")
    List<CuotaPartePorCobrar> findByFechaGeneracionBetween(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Buscar cuotas partes por cédula del pensionado
     */
    List<CuotaPartePorCobrar> findByCedulaPensionadoContaining(String cedula);

    /**
     * Buscar cuotas partes vencidas (fecha vencimiento anterior a hoy y estado PENDIENTE)
     */
    @Query("SELECT c FROM CuotaPartePorCobrar c WHERE c.fechaVencimiento < :fecha AND c.estado = :estado")
    List<CuotaPartePorCobrar> findVencidas(
        @Param("fecha") LocalDate fecha,
        @Param("estado") EstadoCuotaParte estado
    );

    /**
     * Obtener total por cobrar de una entidad deudora
     */
    @Query("SELECT SUM(c.valorCuotaParte) FROM CuotaPartePorCobrar c WHERE c.entidadDeudora.idEntidad = :idEntidad AND c.estado = :estado")
    Double getTotalPorCobrarPorEntidad(
        @Param("idEntidad") Long idEntidad,
        @Param("estado") EstadoCuotaParte estado
    );
}

