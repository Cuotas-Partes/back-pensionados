package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.ILogCambioServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.LogCambioPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.LogCambioRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResultadoCobroPorPeriodoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/logs")
@Tag(name = "Logs", description = "Gestión de logs")
public class LogCambioControlador {
    @Autowired
    private ILogCambioServicio logCambioServicio;

    @PostMapping
    @Operation(summary = "Registrar un log manualmente",
            description = "Permite insertar un registro de log de manera manual")
    @ApiResponse(
            responseCode = "200",
            description = "Registro hecho correctamente",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ResultadoCobroPorPeriodoDTO.class)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Solicitud incorrecta, verifique los parámetros enviados",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor, por favor intente más tarde",
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json"
            )
    )
    public void registrarLogManual(@RequestBody LogCambioPeticion logCambioPeticion){
        logCambioServicio.registrarManualmente(logCambioPeticion);
    }


    /**
     * Obtiene la lista completa de logs.
     *
     * @return Lista de LogCambioRespuesta.
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los logs",
            description = "Devuelve una lista con todos los registros del historial."
    )
    public List<LogCambioRespuesta>listarLogs(){
        return logCambioServicio.obtenerLogs();
    }



    /**
     * Obtiene los logs filtrados por nombre de entidad.
     *
     * @param entidad Nombre de la entidad que se desea filtrar.
     * @return Lista de logs correspondientes a dicha entidad.
     */
    @GetMapping("/entidad/{entidad}")
    @Operation(
            summary = "Filtrar logs por entidad",
            description = "Devuelve los logs asociados a una entidad específica."
    )
    public List<LogCambioRespuesta> filtrarLogsPorEntidad(@PathVariable String entidad){
        return logCambioServicio.obtenerLogsPorEntidad(entidad);
    }

    /**
     * Obtiene los logs filtrados por ID de usuario.
     *
     * @param usuario ID del usuario cuyas acciones se desean consultar.
     * @return Lista de logs realizados por el usuario.
     */
    @GetMapping("/usuario/{usuario}")
    @Operation(
            summary = "Filtrar logs por usuario",
            description = "Devuelve los logs realizados por un usuario específico."
    )
    public List<LogCambioRespuesta> filtrarLogsPorUsuario(@PathVariable Integer usuario){
        return logCambioServicio.obtenerLogsPorUsuario(usuario);
    }





}
