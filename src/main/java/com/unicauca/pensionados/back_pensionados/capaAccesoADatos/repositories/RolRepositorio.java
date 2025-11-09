package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Rol;

//public interface RolRepositorio extends JpaRepository<Rol, Long> {
//}


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}

