package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}