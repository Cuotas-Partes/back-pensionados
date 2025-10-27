package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;

public interface PensionadoRepositorio extends JpaRepository<Pensionado, Long>{
    List<Pensionado> findByNombrePersonaContainingIgnoreCase(String query);
    List<Pensionado> findByApellidosPersonaContainingIgnoreCase(String query);
    List<Pensionado> findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(String nombre, String apellido);

    @Query("SELECT new com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta(" +
    "t.entidad.nitEntidad, t.entidad.nombreEntidad, SUM(p.cuotaParteTotalAnio)) " +
    "FROM Trabajo t " +
    "JOIN t.pensionado pn " +
    "JOIN t.entidad e " +
    "JOIN CuotaParte cp ON t.idTrabajo = cp.trabajo.idTrabajo " +
    "JOIN Periodo p ON cp.idCuotaParte = p.cuotaParte.idCuotaParte " +
    "WHERE pn.numeroIdPersona = :pensionadoId " +
    "AND e.nitEntidad != 8911500319L " +
    "GROUP BY e.nitEntidad, e.nombreEntidad")
    List<EntidadCuotaParteRespuesta> findEntidadesYCuotaParteByPensionadoId(@Param("pensionadoId") Long pensionadoId);
}
