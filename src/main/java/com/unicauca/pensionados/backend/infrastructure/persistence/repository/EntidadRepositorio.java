package com.unicauca.pensionados.backend.infrastructure.persistence.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import java.util.List;
import java.util.Optional;

public interface EntidadRepositorio extends JpaRepository<Entidad, Long> {
    //Manejo de la tabla Entidad
    List<Entidad> findByNameContainingIgnoreCase(String query);
    List<Entidad> findByAddressContainingIgnoreCase(String query);
    List<Entidad> findByEmailContainingIgnoreCase(String query);
    Optional<Entidad> findByNit(String nit);
    Optional<Entidad> findById(Long id);
    Boolean existsByName(String name); //verifica si existe el nombre de la entidad
    Boolean existsByNit(String nit); //verifica si existe el NIT de la entidad
    //Listar todas las entidades
    List<Entidad> findAllByOrderByNitAsc(); // ordena por NIT ascendente
}