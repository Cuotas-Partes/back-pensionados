package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRespositorio extends JpaRepository<Contrato, Long> {
}
