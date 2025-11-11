package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaAnual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuotaAnualRepositorio extends JpaRepository<CuotaAnual, Long> {


    CuotaAnual findByAnio(Long anio);

}
