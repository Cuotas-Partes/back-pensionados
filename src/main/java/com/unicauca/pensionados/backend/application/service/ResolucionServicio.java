package com.unicauca.pensionados.backend.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.application.service.IResolucionServicio;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.ResolucionRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.ResolucionDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ResolucionDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResolucionServicio implements IResolucionServicio {

    private final ResolucionRepositorio resolucionRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;

    @Override
    @Transactional
    public ResolucionDTORespuesta crear(ResolucionDTOPeticion peticion) {
        Pensionado pensionado = pensionadoRepositorio.findById(peticion.getPensionadoId())
                .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado"));

        Resolucion resolucion = new Resolucion();
        resolucion.setNumeroResolucion(peticion.getNumeroResolucion());
        resolucion.setFechaResolucion(peticion.getFechaResolucion());
        resolucion.setValorResolucion(peticion.getValorResolucion());
        resolucion.setEstado(peticion.getEstado());
        resolucion.setTipoResolucion(peticion.getTipoResolucion());
        resolucion.setPensionado(pensionado);
        resolucion.setCreatedAt(LocalDateTime.now());

        resolucion = resolucionRepositorio.save(resolucion);
        return mapearARespuesta(resolucion);
    }

    @Override
    @Transactional
    public ResolucionDTORespuesta actualizar(Long id, ResolucionDTOPeticion peticion) {
        Resolucion resolucion = resolucionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Resolución no encontrada"));

        resolucion.setNumeroResolucion(peticion.getNumeroResolucion());
        resolucion.setFechaResolucion(peticion.getFechaResolucion());
        resolucion.setValorResolucion(peticion.getValorResolucion());
        resolucion.setEstado(peticion.getEstado());
        resolucion.setTipoResolucion(peticion.getTipoResolucion());
        resolucion.setUpdatedAt(LocalDateTime.now());

        if (!resolucion.getPensionado().getIdPersona().equals(peticion.getPensionadoId())) {
            Pensionado pensionado = pensionadoRepositorio.findById(peticion.getPensionadoId())
                    .orElseThrow(() -> new RecursoNoEncontrado("Pensionado no encontrado"));
            resolucion.setPensionado(pensionado);
        }

        resolucion = resolucionRepositorio.save(resolucion);
        return mapearARespuesta(resolucion);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!resolucionRepositorio.existsById(id)) {
            throw new RecursoNoEncontrado("Resolución no encontrada");
        }
        resolucionRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ResolucionDTORespuesta obtenerPorId(Long id) {
        Resolucion resolucion = resolucionRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("Resolución no encontrada"));
        return mapearARespuesta(resolucion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResolucionDTORespuesta> obtenerTodas() {
        return resolucionRepositorio.findAll().stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResolucionDTORespuesta> obtenerPorPensionado(Long pensionadoId) {
        return resolucionRepositorio.findByPensionadoIdPersona(pensionadoId).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResolucionDTORespuesta> obtenerPorEstado(String estado) {
        return resolucionRepositorio.findByEstado(estado).stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResolucionDTORespuesta obtenerPorNumero(String numeroResolucion) {
        Resolucion resolucion = resolucionRepositorio.findByNumeroResolucion(numeroResolucion)
                .orElseThrow(() -> new RecursoNoEncontrado("Resolución no encontrada"));
        return mapearARespuesta(resolucion);
    }

    private ResolucionDTORespuesta mapearARespuesta(Resolucion resolucion) {
        ResolucionDTORespuesta respuesta = new ResolucionDTORespuesta();
        respuesta.setId(resolucion.getId());
        respuesta.setNumeroResolucion(resolucion.getNumeroResolucion());
        respuesta.setFechaResolucion(resolucion.getFechaResolucion());
        respuesta.setValorResolucion(resolucion.getValorResolucion());
        respuesta.setEstado(resolucion.getEstado());
        respuesta.setTipoResolucion(resolucion.getTipoResolucion());
        respuesta.setPensionadoId(resolucion.getPensionado().getIdPersona());
        respuesta.setPensionadoNombre(resolucion.getPensionado().getNombrePersona() + " " + resolucion.getPensionado().getApellidosPersona());
        respuesta.setCreatedAt(resolucion.getCreatedAt());
        respuesta.setUpdatedAt(resolucion.getUpdatedAt());
        return respuesta;
    }
}
