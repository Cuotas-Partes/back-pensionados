package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.util.List;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PersonaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.SucesorRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Sucesor;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroSucesorPeticion;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class SucesorServicio implements ISucesorServicio {
    @Autowired
    private PersonaRepositorio personaRepositorio;
    @Autowired
    private SucesorRepositorio sucesorRepositorio;
    @Autowired
    private PensionadoRepositorio pensionadoRepositorio;

    /**
     * Registra un sucesor.
     * 
     * @param request Objeto que contiene la información del sucesor a registrar.
     * @throws RuntimeException si el número de identificación ya está registrado o
     *                          si la entidad no está registrada.
     * @throws Exception        si ocurre un error al registrar el sucesor.
     */
    @Transactional
    @Override
    public void registrarSucesor(RegistroSucesorPeticion request) {
        // Validar si ya existe un sucesor con el mismo número de identificación
        if (personaRepositorio.existsById(request.getNumeroIdPersona())) {
            throw new RuntimeException("El número de identificación ya está registrado");
        }
        // Validar si el pensionado esta registrado
        Pensionado pensionado = pensionadoRepositorio.findById(request.getPensionado())
                .orElseThrow(() -> new RuntimeException("El pensionado no está registrado"));

        // Crear el sucesor
        Sucesor sucesor = new Sucesor();
        sucesor.setNumeroIdPersona(request.getNumeroIdPersona());
        sucesor.setTipoIdPersona(request.getTipoIdPersona());
        sucesor.setNombrePersona(request.getNombrePersona());
        sucesor.setApellidosPersona(request.getApellidosPersona());
        sucesor.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        sucesor.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        sucesor.setEstadoPersona(request.getEstadoPersona());
        sucesor.setGeneroPersona(request.getGeneroPersona());
        sucesor.setFechaInicioSucesion(request.getFechaInicioSucesion());
        sucesor.setPorcentajePension(request.getPorcentajePension());
        sucesor.setPensionado(pensionado);
        // Guardar el sucesor
        sucesorRepositorio.save(sucesor);
    }

    /**
     * Lista todos los sucesores registrados.
     * 
     * @return Lista de sucesores registrados.
     */
    @Override
    public List<RegistroSucesorPeticion> listaSucesores() {
        List<Sucesor> sucesores = sucesorRepositorio.findAll();
        return sucesores.stream().map(sucesor -> {
            RegistroSucesorPeticion request = new RegistroSucesorPeticion();
            request.setNumeroIdPersona(sucesor.getNumeroIdPersona());
            request.setTipoIdPersona(sucesor.getTipoIdPersona());
            request.setNombrePersona(sucesor.getNombrePersona());
            request.setApellidosPersona(sucesor.getApellidosPersona());
            request.setFechaNacimientoPersona(sucesor.getFechaNacimientoPersona());
            request.setFechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona());
            request.setEstadoPersona(sucesor.getEstadoPersona());
            request.setGeneroPersona(sucesor.getGeneroPersona());
            request.setFechaInicioSucesion(sucesor.getFechaInicioSucesion());
            request.setPensionado(sucesor.getPensionado().getNumeroIdPersona());
            request.setPorcentajePension(sucesor.getPorcentajePension());
            return request;
        }).toList();
    }

    /**
     * Obtiene un sucesor por su ID.
     * 
     * @param id SucesorId del sucesor a obtener.
     * @return Objeto que contiene la información del sucesor.
     * @throws RuntimeException si el sucesor no está registrado.
     * @throws Exception        si ocurre un error al obtener el sucesor.
     */
    @Override
    public RegistroSucesorPeticion obtenerSucesorPorId(Long id) {
        // Validar si el sucesor existe
        Sucesor sucesor = sucesorRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("El sucesor no está registrado"));

        // Crear el objeto de respuesta
        RegistroSucesorPeticion request = new RegistroSucesorPeticion();
        request.setNumeroIdPersona(sucesor.getNumeroIdPersona());
        request.setTipoIdPersona(sucesor.getTipoIdPersona());
        request.setNombrePersona(sucesor.getNombrePersona());
        request.setApellidosPersona(sucesor.getApellidosPersona());
        request.setFechaNacimientoPersona(sucesor.getFechaNacimientoPersona());
        request.setFechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona());
        request.setEstadoPersona(sucesor.getEstadoPersona());
        request.setGeneroPersona(sucesor.getGeneroPersona());
        request.setFechaInicioSucesion(sucesor.getFechaInicioSucesion());
        request.setPensionado(sucesor.getPensionado().getNumeroIdPersona());
        request.setPorcentajePension(sucesor.getPorcentajePension());

        return request;
    }

    /**
     * Edita los datos de un sucesor existente.
     * 
     * @param id      El ID del sucesor que se desea editar. Este ID se utiliza para
     *                buscar el sucesor en la base de datos.
     * @param request Un objeto de tipo RegistroSucesorPeticion que contiene los
     *                nuevos datos que se desean actualizar en el sucesor.
     * @throws RuntimeException Si el sucesor con el ID especificado no está
     *                          registrado en la base de datos.
     */
    @Override
    public void editarSucesor(Long id, RegistroSucesorPeticion request) {
        // Validar si el sucesor existe
        Sucesor sucesor = sucesorRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("El sucesor no está registrado"));

        // Actualizar los campos del sucesor
        sucesor.setTipoIdPersona(request.getTipoIdPersona());
        sucesor.setNombrePersona(request.getNombrePersona());
        sucesor.setApellidosPersona(request.getApellidosPersona());
        sucesor.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        sucesor.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        sucesor.setEstadoPersona(request.getEstadoPersona());
        sucesor.setGeneroPersona(request.getGeneroPersona());
        sucesor.setFechaInicioSucesion(request.getFechaInicioSucesion());

        // Guardar los cambios
        sucesorRepositorio.save(sucesor);
    }

    /**
     * Elimina un sucesor por su ID.
     * 
     * @param id SucesorId del sucesor a eliminar.
     * @throws RuntimeException si el sucesor no está registrado.
     * @throws Exception        si ocurre un error al eliminar el sucesor.
     */
    @Override
    public void eliminarSucesor(Long id) {
        // Validar si el sucesor existe
        if (!sucesorRepositorio.existsById(id)) {
            throw new RuntimeException("El sucesor no está registrado");
        }
        // Eliminar el sucesor
        sucesorRepositorio.deleteById(id);
    }
}