package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.Rol;

//public interface RolRepositorio extends JpaRepository<Rol, Long> {
//}


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.backend.domain.model.entity.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}

