package com.unicauca.pensionados.backend.application.service.interfaces;

import java.time.LocalDate;


import com.unicauca.pensionados.backend.domain.model.entity.CuotaParte;
import com.unicauca.pensionados.backend.domain.model.entity.Periodo;


public interface IPeriodoServicio {

    void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte);

    Periodo findPeriodoByFechas(LocalDate fechaInicio, LocalDate fechaFin);
}