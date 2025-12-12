package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.pensionados.backend.domain.model.entity.Pago;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPago;

public interface PagoRepositorio extends JpaRepository<Pago, Long> {
    
    // Consultas por entidad
    List<Pago> findByEntidadNit(String nit);

    // Consultas por año
    List<Pago> findByAnio(Integer anio);
    List<Pago> findByAnioBetween(Integer anioDesde, Integer anioHasta);
    
    // Consultas por entidad y año
    List<Pago> findByEntidadNitAndAnio(String nit, Integer anio);
    List<Pago> findByEntidadNitAndAnioBetween(String nit, Integer anioDesde, Integer anioHasta);

    // Consultas por rango de fechas
    List<Pago> findByFechaPagoBetween(LocalDate fechaDesde, LocalDate fechaHasta);
    List<Pago> findByEntidadNitAndFechaPagoBetween(String nit, LocalDate fechaDesde, LocalDate fechaHasta);

    // Consultas por estado de verificación
    List<Pago> findByVerificado(Boolean verificado);
    List<Pago> findByEntidadNitAndVerificado(String nit, Boolean verificado);

    // Consultas por tipo de pago
    List<Pago> findByTipoPago(TipoPago tipoPago);
    List<Pago> findByEntidadNitAndTipoPago(String nit, TipoPago tipoPago);

    // Consulta para verificar duplicados (misma entidad, mismo año, misma fecha)
    boolean existsByEntidadNitAndAnioAndFechaPago(String nit, Integer anio, LocalDate fechaPago);

    // Consulta para obtener pagos por pensionado
    List<Pago> findByPensionadoIdPersona(Long idPensionado);
    
    // Consulta combinada para filtros complejos
    @Query("SELECT p FROM Pago p WHERE " +
           "(:nitEntidad IS NULL OR p.entidad.nit = :nitEntidad) AND " +
           "(:anioDesde IS NULL OR p.anio >= :anioDesde) AND " +
           "(:anioHasta IS NULL OR p.anio <= :anioHasta) AND " +
           "(:fechaDesde IS NULL OR p.fechaPago >= :fechaDesde) AND " +
           "(:fechaHasta IS NULL OR p.fechaPago <= :fechaHasta) AND " +
           "(:verificado IS NULL OR p.verificado = :verificado) AND " +
           "(:tipoPago IS NULL OR p.tipoPago = :tipoPago) AND " +
           "(:idPensionado IS NULL OR p.pensionado.idPersona = :idPensionado) " +
           "ORDER BY p.anio DESC, p.fechaPago DESC")
    List<Pago> buscarConFiltros(
        @Param("nitEntidad") String nitEntidad,
        @Param("anioDesde") Integer anioDesde,
        @Param("anioHasta") Integer anioHasta,
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta,
        @Param("verificado") Boolean verificado,
        @Param("tipoPago") TipoPago tipoPago,
        @Param("idPensionado") Long idPensionado
    );
}

