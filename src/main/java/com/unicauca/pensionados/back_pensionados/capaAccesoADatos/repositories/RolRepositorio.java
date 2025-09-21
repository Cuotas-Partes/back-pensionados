package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
}