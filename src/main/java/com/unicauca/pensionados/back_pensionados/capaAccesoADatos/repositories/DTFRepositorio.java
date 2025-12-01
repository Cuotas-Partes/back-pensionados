package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.DTF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DTFRepositorio extends JpaRepository<DTF, Long> {

    @Query("SELECT d FROM DTF d WHERE (:mes IS NULL OR d.mes = :mes) AND (:anio IS NULL OR d.anio = :anio)")
    List<DTF> findByMesAndAnio(@Param("mes") Long mes, @Param("anio") Long anio);

}
