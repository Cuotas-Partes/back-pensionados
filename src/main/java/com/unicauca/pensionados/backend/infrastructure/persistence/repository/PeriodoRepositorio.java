package com.unicauca.pensionados.backend.infrastructure.persistence.repository;


import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PeriodoRepositorio extends JpaRepository<Periodo, Long> {
    @Operation(
        summary = "Buscar periodo por año",
        description = "Este endpoint permite buscar un periodo específico basado en el año de inicio del periodo."
    )
    @Query("SELECT p FROM Periodo p WHERE YEAR(p.fechaInicioPeriodo) = :anio")
    Optional<Periodo> findByAnio(int anio);

    @Operation(
        summary = "Eliminar periodos por ID de cuota parte",
        description = "Este endpoint permite eliminar todos los periodos asociados a una cuota parte específica utilizando el ID de la cuota parte."
    )
    void deleteByCuotaParte_IdCuotaParte(Long idCuotaParte);

    @Query("""
            SELECT p FROM Periodo p where p.fechaInicioPeriodo = :fechaInicio AND p.fechaFinPeriodo = :fechaFin
            """)
    Optional<Periodo> findPeriodoByFechas(@Param("fechaInicio") LocalDate fechaInicio, @Param("fechaFin") LocalDate fechaFin);
}