package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
//import com.unicauca.pensionados.backend.domain.model.entity.Trabajo.TrabajoId;

public interface TrabajoRepositorio extends JpaRepository<Trabajo, Long> {
    
    Optional<Trabajo> findByPensionadoAndEntidad(Pensionado pensionado, Entidad entidad);
    //List<Trabajo> findByPensionadoAndEntidad(Pensionado pensionado, Entidad entidad);
    List<Trabajo> findByEntidadNit(String nit);
    List<Trabajo> findByPensionado(Pensionado pensionado);
    Optional<Trabajo> findByPensionadoAndEntidad(Pensionado pensionadoExistente, Optional<Entidad> entidadAnterior);

}
////