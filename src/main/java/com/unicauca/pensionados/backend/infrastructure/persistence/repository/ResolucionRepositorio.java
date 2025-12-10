package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;

public interface ResolucionRepositorio extends JpaRepository<Resolucion, Long> {
    
    Optional<Resolucion> findByNumeroResolucion(String numeroResolucion);
    
    List<Resolucion> findByPensionadoIdPersona(Long pensionadoId);
    
    List<Resolucion> findByEstado(String estado);
    
    List<Resolucion> findByTipoResolucion(String tipoResolucion);
}
