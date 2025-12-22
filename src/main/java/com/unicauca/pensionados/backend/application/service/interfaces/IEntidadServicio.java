package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.application.dto.request.entidad.RegistroEntidadPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroTrabajoPeticion;

import java.util.List;


public interface IEntidadServicio {
    List<Entidad> buscarEntidadesPorCriterio(String query);
    List<Entidad> buscarEntidadPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    List<Entidad> listarTodos();

    void registrarEntidad(RegistroEntidadPeticion request);
    void actualizar(Long idEntidad, RegistroEntidadPeticion entidad);
    void eliminar(Long idEntidad);

    boolean activarEntidad(Long nid);
    boolean desactivarEntidad(Long nid);
    void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados);
}
