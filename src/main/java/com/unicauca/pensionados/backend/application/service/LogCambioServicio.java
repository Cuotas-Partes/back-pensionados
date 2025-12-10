package com.unicauca.pensionados.backend.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicauca.pensionados.backend.domain.model.entity.LogCambio;
import com.unicauca.pensionados.backend.domain.model.entity.Usuario;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.LogCambioRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.UsuarioRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.LogCambioPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LogCambioRespuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de registrar logs de auditoría asociados a las operaciones
 * CRUD realizadas sobre diferentes entidades del sistema.
 *
 * Este servicio permite registrar acciones como creación, actualización,
 * eliminación y consultas, almacenando información relevante como la entidad
 * afectada, valores antes y después, fecha del evento y el usuario que ejecutó la acción.
 */
@Service
public class LogCambioServicio implements ILogCambioServicio {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LogCambioRepositorio logCambioRepositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    /**
     * Registra un log correspondiente a la creación de una entidad.
     *
     * @param nombreEntidad Nombre de la entidad sobre la cual se realiza la operación.
     * @param valorNuevo    Objeto que representa el estado nuevo de la entidad creada.
     * @throws IllegalArgumentException si alguno de los parámetros es nulo.
     */
    @Override
    public void registrarCreacion(String nombreEntidad, Object valorNuevo) {
        validar(nombreEntidad, valorNuevo);
        registrarLog(nombreEntidad, LogCambio.Accion.CREAR, null, valorNuevo);
    }

    /**
     * Registra un log correspondiente a la actualización de una entidad.
     *
     * @param nombreEntidad Nombre de la entidad que fue actualizada.
     * @param valorAnterior Estado anterior de la entidad.
     * @param valorNuevo    Estado actualizado de la entidad.
     * @throws IllegalArgumentException si alguno de los parámetros es nulo.
     */
    @Override
    public void registrarActualizacion(String nombreEntidad, Object valorAnterior, Object valorNuevo) {
        validar(nombreEntidad, valorAnterior, valorNuevo);
        registrarLog(nombreEntidad, LogCambio.Accion.ACTUALIZAR, valorAnterior, valorNuevo);
    }

    /**
     * Registra un log correspondiente a la eliminación de una entidad.
     *
     * @param nombreEntidad Nombre de la entidad eliminada.
     * @param valorAnterior Estado de la entidad previo a ser eliminada.
     * @throws IllegalArgumentException si algún parámetro es nulo.
     */
    @Override
    public void registrarEliminacion(String nombreEntidad, Object valorAnterior) {
        validar(nombreEntidad, valorAnterior);
        registrarLog(nombreEntidad, LogCambio.Accion.ELIMINAR, valorAnterior, null);
    }

    /**
     * Registra un log correspondiente a una consulta realizada sobre una entidad.
     *
     * @param nombreEntidad Nombre de la entidad consultada.
     * @throws IllegalArgumentException si el nombre de la entidad es nulo.
     */
    @Override
    public void registrarConsulta(String nombreEntidad) {
        validar(nombreEntidad);
        registrarLog(nombreEntidad, LogCambio.Accion.CONSULTAR, null, null);
    }

    public void registrarManualmente(LogCambioPeticion logCambioPeticion) {

        if (logCambioPeticion.getAccion() == null) {
            throw new IllegalArgumentException("La acción no puede ser nula");
        }
        if (logCambioPeticion.getEntidad() == null || logCambioPeticion.getEntidad().isBlank()) {
            throw new IllegalArgumentException("La entidad no puede ser nula o vacía");
        }


        LogCambio.Accion tipoAccion;
        try {
            tipoAccion = LogCambio.Accion.valueOf(logCambioPeticion.getAccion().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo de la acción no es válido");
        }

        if (logCambioPeticion.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        Usuario usuario = usuarioRepositorio.findById(logCambioPeticion.getUsuarioId()).orElseThrow(()->new RuntimeException("Usuario no encontrado"));

        LogCambio logCambio = LogCambio.builder().entidad(logCambioPeticion.getEntidad())
                .accion(tipoAccion).fecha(LocalDateTime.now()).usuario(usuario).build();

        switch (tipoAccion) {
            case CREAR:
                if (logCambioPeticion.getValorNuevo() == null) {
                    throw new IllegalArgumentException("En CREAR debe enviarse valorNuevo.");
                }
                logCambio.setValorNuevo(objetoAJson(logCambioPeticion.getValorNuevo()));
                break;

                case ACTUALIZAR:
                    if (logCambioPeticion.getValorAnterior() == null || logCambioPeticion.getValorNuevo() == null) {
                        throw new IllegalArgumentException("En ACTUALIZAR debe enviarse valorAnterior y valorNuevo.");
                    }
                    logCambio.setValorAnterior(objetoAJson(logCambioPeticion.getValorAnterior()));
                    logCambio.setValorNuevo(objetoAJson(logCambioPeticion.getValorNuevo()));
                    break;

                    case ELIMINAR:
                        if (logCambioPeticion.getValorAnterior() == null) {
                            throw new IllegalArgumentException("En ELIMINAR debe enviarse valorAnterior.");
                        }
                        logCambio.setValorAnterior(objetoAJson(logCambioPeticion.getValorAnterior()));
                        break;

            default:
                throw new IllegalStateException("Acción no manejada: " + tipoAccion);
        }

        logCambioRepositorio.save(logCambio);
    }


    public List<LogCambioRespuesta>obtenerLogs(){
        return logCambioRepositorio.findAll()
                .stream()
                .map(this::entidadARespuesta)
                .collect(Collectors.toList());
    }

    public List<LogCambioRespuesta> obtenerLogsPorUsuario(Integer idUsuario){
        return logCambioRepositorio.findByUsuario_Id(idUsuario)
                .stream()
                .map(this::entidadARespuesta)
                .collect(Collectors.toList());
    }

    public List<LogCambioRespuesta> obtenerLogsPorEntidad(String nombreEntidad){
        return logCambioRepositorio.findByEntidad(nombreEntidad)
                .stream()
                .map(this::entidadARespuesta)
                .collect(Collectors.toList());
    }


    private LogCambioRespuesta entidadARespuesta(LogCambio logCambio){
        LogCambioRespuesta logCambioRespuesta = LogCambioRespuesta.builder()
                .id(logCambio.getId()).entidad(logCambio.getEntidad()).accion(logCambio.getAccion().name())
                .valorAnterior(logCambio.getValorAnterior()).valorNuevo(logCambio.getValorNuevo())
                .fecha(logCambio.getFecha()).usuarioId(logCambio.getUsuario().getId()).build();
        return logCambioRespuesta;
    }



    private void registrarLog(String entidad,
                              LogCambio.Accion accion,
                              Object valorAnterior,
                              Object valorNuevo) {

        LogCambio log = LogCambio.builder()
                .entidad(entidad)
                .accion(accion)
                .valorAnterior(objetoAJson(valorAnterior))
                .valorNuevo(objetoAJson(valorNuevo))
                .fecha(LocalDateTime.now())
                .usuario(obtenerUsuarioLogueado())
                .build();

        logCambioRepositorio.save(log);
    }
    /**
     * Valida valores de objetos
     * @param valores Parámetros de tipo Objeto
     */
    private void validar(Object... valores) {
        for (Object v : valores) {
            if (v == null) {
                throw new IllegalArgumentException("No se pudo crear log debido a parámetros inválidos");
            }
        }
    }

    /**
     * Convierte un objeto Java a su representación JSON mediante {@link ObjectMapper}.
     *
     * @param valor Objeto a serializar.
     * @return Cadena JSON representando el objeto, o un mensaje de error en caso de fallo.
     */
    private String objetoAJson(Object valor) {
        String valorNuevoJson;
        try {
            valorNuevoJson = objectMapper.writeValueAsString(valor);
        } catch (Exception e) {
            valorNuevoJson = "No se pudo serializar el objeto: " + e.getMessage();
        }
        return valorNuevoJson;
    }


    /**
     * Obtiene el usuario actualmente autenticado desde el contexto de seguridad de Spring.
     *
     * @return Usuario autenticado.
     * @throws UsernameNotFoundException si no existe un usuario autenticado o no es del tipo esperado.
     */
    private Usuario obtenerUsuarioLogueado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Usuario) {
            Usuario usuario = (Usuario) authentication.getPrincipal();
            return usuario; // <-- aquí tienes el ID del usuario
        }
        throw new UsernameNotFoundException("Usuario no encontrado");
    }

}
