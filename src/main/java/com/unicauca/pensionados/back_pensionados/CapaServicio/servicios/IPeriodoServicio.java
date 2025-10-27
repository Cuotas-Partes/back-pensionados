package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.time.LocalDate;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;


public interface IPeriodoServicio {

    void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte);
}