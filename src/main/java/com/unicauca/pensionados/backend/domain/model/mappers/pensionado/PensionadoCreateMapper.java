package com.unicauca.pensionados.backend.domain.model.mappers.pensionado;

import com.unicauca.pensionados.backend.application.dto.request.pensionado.RegistroPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.resolucion.ResolucionDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.request.sucesor.RegistroSucesorPeticion;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;

public class PensionadoCreateMapper {
    public static Pensionado toEntity(RegistroPensionadoPeticion dto) {
        Pensionado p = new Pensionado();
        p.setCedula(dto.getCedula());
        p.setFechaExpedicionCedula(dto.getFechaExpedicionCedula());
        p.setNombre(dto.getNombre());
        p.setTelefono(dto.getTelefono());
        p.setEntityId(dto.getEntityId());
        p.setCorreo(dto.getCorreo());
        p.setApellidos(dto.getApellidos());
        p.setFechaNacimiento(dto.getFechaNacimiento());
        p.setTipoJubilacion(dto.getTipoJubilacion());
        p.setDiasTotalesTrabajados(dto.getDiasTotalesTrabajados());
        p.setDiasTrabajadosEntidad(dto.getDiasTrabajadosEntidad());
        p.setFechaFallecimiento(dto.getFechaFallecimiento());

        p.setEstado(EstadoPersona.Activo);
        return p;
    }

    public static Resolucion toResolucionEntity(ResolucionDTOPeticion dto, Pensionado p) {
        Resolucion r = new Resolucion();
        r.setNumeroResolucion(dto.getNumeroResolucion());
        r.setFechaResolucion(dto.getFechaResolucion());
        r.setTipoResolucion(dto.getTipoResolucion());
        r.setValorResolucion(dto.getValorResolucion());
        r.setEstado(EstadoResolucion.valueOf("Vigente"));
        r.setPensionado(p);
        return r;
    }

    public static Sucesor toSucesorEntity(RegistroSucesorPeticion dto, Pensionado p, Resolucion resolucion) {
        Sucesor s = new Sucesor();
        s.setNumeroDocumento(dto.getNumeroDocumento());
        s.setTipoIdentificacion(dto.getTipoIdentificacion());
        s.setNombreCompleto(dto.getNombreCompleto());
        s.setTelefono(dto.getTelefono());
        s.setFechaInicio(dto.getFechaInicio());
        s.setPorcentajePension(dto.getPorcentajePension());
        s.setResolucionNombramiento(resolucion);
        s.setPensionadoSustituido(p);
        return s;
    }
}
