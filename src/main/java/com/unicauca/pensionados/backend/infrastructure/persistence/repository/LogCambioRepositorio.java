package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.LogCambio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogCambioRepositorio extends JpaRepository<LogCambio,Long> {
    List<LogCambio> findByEntidad(String entidad);
    List<LogCambio> findByUsuario_Id(Integer id);

}
