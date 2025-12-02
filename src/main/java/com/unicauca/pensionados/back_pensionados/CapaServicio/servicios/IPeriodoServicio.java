package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.time.LocalDate;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;


public interface IPeriodoServicio {

    void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte);

    Periodo findPeriodoByFechas(LocalDate fechaInicio, LocalDate fechaFin);
}