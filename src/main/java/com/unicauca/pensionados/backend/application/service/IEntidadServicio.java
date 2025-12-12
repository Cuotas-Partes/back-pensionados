package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.application.dto.request.RegistroEntidadPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroTrabajoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.EntidadConPensionadosRespuesta;

import java.util.List;

import org.springframework.stereotype.Service;


public interface IEntidadServicio {
    List<Entidad> buscarEntidadesPorCriterio(String query);
    List<Entidad> buscarEntidadPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    //List<Entidad> listarTodos();
    List<Entidad> listarTodos();
    void registrarEntidad(RegistroEntidadPeticion request);
    void actualizar(Long nid, RegistroEntidadPeticion entidad);
    boolean activarEntidad(Long nid);
    boolean desactivarEntidad(Long nid);
    void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados);
}