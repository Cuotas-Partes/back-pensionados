package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    
}
