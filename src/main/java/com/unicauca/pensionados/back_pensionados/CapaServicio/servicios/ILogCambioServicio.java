package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;


import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LogCambioPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.LogCambioRespuesta;

import java.util.List;

public interface ILogCambioServicio {
    void registrarCreacion(String nombreEntidad, Object valorNuevo);
    void registrarActualizacion(String nombreEntidad, Object valorAnterior,Object valorNuevo);
    void registrarEliminacion(String nombreEntidad, Object valorAnterior);
    void registrarConsulta(String nombreEntidad);
    void registrarManualmente(LogCambioPeticion logCambioPeticion);
    List<LogCambioRespuesta> obtenerLogs();
    List<LogCambioRespuesta> obtenerLogsPorUsuario(Integer idUsuario);
    List<LogCambioRespuesta> obtenerLogsPorEntidad(String nombreEntidad);
}
