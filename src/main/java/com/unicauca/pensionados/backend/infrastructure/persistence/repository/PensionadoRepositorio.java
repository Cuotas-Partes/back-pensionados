package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.application.dto.response.entidad.EntidadCuotaParteRespuesta;

public interface PensionadoRepositorio extends JpaRepository<Pensionado, Long>{
    @Operation(
        summary = "Buscar pensionados por nombre",
        description = "Este endpoint permite buscar pensionados cuyo nombre contenga una cadena de texto específica, sin distinguir entre mayúsculas y minúsculas."
    )
    List<Pensionado> findByNombreContainingIgnoreCase(String query);
    @Operation(
        summary = "Buscar pensionados por apellidos",
        description = "Este endpoint permite buscar pensionados cuyos apellidos contengan una cadena de texto específica, sin distinguir entre mayúsculas y minúsculas."
    )
    List<Pensionado> findByApellidosContainingIgnoreCase(String query);
    @Operation(
        summary = "Buscar pensionados por nombre o apellidos",
        description = "Este endpoint permite buscar pensionados cuyo nombre o apellidos contengan una cadena de texto específica, sin distinguir entre mayúsculas y minúsculas."
    )
    List<Pensionado> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(String nombre, String apellido);

    // Buscar pensionado por cédula (número de identificación)
    Optional<Pensionado> findByCedula(String cedula);

    List<Pensionado> findByEntityId(@Param("entity_id") Long entidadId);
}
