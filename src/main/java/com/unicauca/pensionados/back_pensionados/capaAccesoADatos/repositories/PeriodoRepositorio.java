package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface PeriodoRepositorio extends JpaRepository<Periodo, Long> {
    @Query("SELECT p FROM Periodo p WHERE YEAR(p.fechaInicioPeriodo) = :anio")
    Optional<Periodo> findByAnio(int anio);

    void deleteByCuotaParte_IdCuotaParte(Long idCuotaParte);
}