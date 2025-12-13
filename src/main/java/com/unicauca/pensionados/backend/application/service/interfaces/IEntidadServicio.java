package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.application.dto.request.RegistroEntidadPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroTrabajoPeticion;

import java.util.List;


public interface IEntidadServicio {
    List<Entidad> buscarEntidadesPorCriterio(String query);
    List<Entidad> buscarEntidadPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    Entidad buscarPorId(Long id);
    //List<Entidad> listarTodos();
    List<Entidad> listarTodos();
    void registrarEntidad(RegistroEntidadPeticion request);
    void actualizar(Long nid, RegistroEntidadPeticion entidad);
    boolean activarEntidad(Long nid);
    boolean desactivarEntidad(Long nid);
    void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados);
}