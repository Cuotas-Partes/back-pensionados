package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Sucesor;

public interface SucesorRepositorio extends JpaRepository<Sucesor, Long> {
     
}
