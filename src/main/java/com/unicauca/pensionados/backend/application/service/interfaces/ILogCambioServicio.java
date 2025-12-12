package com.unicauca.pensionados.backend.application.service.interfaces;


import com.unicauca.pensionados.backend.application.dto.request.LogCambioPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LogCambioRespuesta;

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
