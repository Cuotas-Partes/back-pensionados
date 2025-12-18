package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.CuotaPartePorPagar;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaParte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para gestionar las cuotas partes por pagar
 */
@Repository
public interface CuotaPartePorPagarRepository extends JpaRepository<CuotaPartePorPagar, Long> {

    /**
     * Buscar cuotas partes por pensionado
     */
    List<CuotaPartePorPagar> findByPensionado_IdPersona(Long idPensionado);

    /**
     * Buscar cuotas partes por entidad acreedora
     */
    List<CuotaPartePorPagar> findByEntidadAcreedora_IdEntidad(Long idEntidad);

    /**
     * Buscar cuotas partes por periodo (año y mes)
     */
    List<CuotaPartePorPagar> findByAnioPeriodoAndMesPeriodo(Integer anio, Integer mes);

    /**
     * Buscar cuotas partes por año
     */
    List<CuotaPartePorPagar> findByAnioPeriodo(Integer anio);

    /**
     * Buscar cuotas partes por estado
     */
    List<CuotaPartePorPagar> findByEstado(EstadoCuotaParte estado);

    /**
     * Buscar cuotas partes pendientes por entidad acreedora
     */
    List<CuotaPartePorPagar> findByEntidadAcreedora_IdEntidadAndEstado(Long idEntidad, EstadoCuotaParte estado);

    /**
     * Buscar cuotas partes por pensionado y periodo
     */
    List<CuotaPartePorPagar> findByPensionado_IdPersonaAndAnioPeriodoAndMesPeriodo(
        Long idPensionado, Integer anio, Integer mes
    );

    /**
     * Buscar cuotas partes por rango de fechas
     */
    @Query("SELECT c FROM CuotaPartePorPagar c WHERE c.fechaGeneracion BETWEEN :fechaInicio AND :fechaFin")
    List<CuotaPartePorPagar> findByFechaGeneracionBetween(
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );

    /**
     * Buscar cuotas partes por cédula del pensionado
     */
    List<CuotaPartePorPagar> findByCedulaPensionadoContaining(String cedula);

    /**
     * Buscar cuotas partes vencidas (fecha vencimiento anterior a hoy y estado PENDIENTE)
     */
    @Query("SELECT c FROM CuotaPartePorPagar c WHERE c.fechaVencimiento < :fecha AND c.estado = :estado")
    List<CuotaPartePorPagar> findVencidas(
        @Param("fecha") LocalDate fecha,
        @Param("estado") EstadoCuotaParte estado
    );

    /**
     * Obtener total por pagar a una entidad acreedora
     */
    @Query("SELECT SUM(c.valorCuotaParte) FROM CuotaPartePorPagar c WHERE c.entidadAcreedora.idEntidad = :idEntidad AND c.estado = :estado")
    Double getTotalPorPagarPorEntidad(
        @Param("idEntidad") Long idEntidad,
        @Param("estado") EstadoCuotaParte estado
    );
}

