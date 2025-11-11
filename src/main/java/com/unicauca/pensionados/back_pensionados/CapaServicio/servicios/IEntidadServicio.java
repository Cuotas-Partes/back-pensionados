package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadConPensionadosRespuesta;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface IEntidadServicio {
    List<EntidadConPensionadosRespuesta> buscarEntidadesPorCriterio(String query);
    List<Entidad> buscarEntidadPorNombre(String nombre);
    Entidad buscarPorNit(Long nit);
    //List<Entidad> listarTodos();
    List<EntidadConPensionadosRespuesta> listarTodos();
    void registrarEntidad(RegistroEntidadPeticion request);
    void actualizar(Long nid, RegistroEntidadPeticion entidad);
    boolean activarEntidad(Long nid);
    boolean desactivarEntidad(Long nid);
    void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados);
}