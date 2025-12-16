package com.unicauca.pensionados.backend.application.service.interfaces;

import java.time.LocalDate;


public interface IPeriodoServicio {

    void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte);

    Periodo findPeriodoByFechas(LocalDate fechaInicio, LocalDate fechaFin);
}