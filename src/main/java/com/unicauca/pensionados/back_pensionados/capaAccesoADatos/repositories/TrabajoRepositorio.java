package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
//import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo.TrabajoId;

public interface TrabajoRepositorio extends JpaRepository<Trabajo, Long> {
    
    Optional<Trabajo> findByPensionadoAndEntidad(Pensionado pensionado, Entidad entidad);
    //List<Trabajo> findByPensionadoAndEntidad(Pensionado pensionado, Entidad entidad);
    List<Trabajo> findByEntidadNitEntidad(Long nitEntidad);
    List<Trabajo> findByPensionado(Pensionado pensionado);
    Optional<Trabajo> findByPensionadoAndEntidad(Pensionado pensionadoExistente, Optional<Entidad> entidadAnterior);
}
////