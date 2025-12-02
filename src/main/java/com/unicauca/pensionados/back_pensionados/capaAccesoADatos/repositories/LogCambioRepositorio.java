package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.LogCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogCambioRepositorio extends JpaRepository<LogCambio,Long> {
    List<LogCambio> findByEntidad(String entidad);
    List<LogCambio> findByUsuario_Id(Integer id);

}
