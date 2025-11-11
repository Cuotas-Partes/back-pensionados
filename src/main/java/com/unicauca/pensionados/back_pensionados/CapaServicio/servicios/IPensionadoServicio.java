package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;

import java.util.List;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

public interface IPensionadoServicio {
    void registrarPensionado (RegistroPensionadoPeticion request);
    void actualizarPensionado (Long id, RegistroPensionadoPeticion request);
    List<PensionadoRespuesta> listarPensionados();
    List<Pensionado> buscarPensionadosPorNombre(String nombre);
    List<Pensionado> buscarPensionadosPorApellido(String apellido);
    List<Pensionado> buscarPensionadosPorCriterio(String query);
    PensionadoRespuesta buscarPensionadoPorId(Long id);
    void desactivarPensionado(Long id);
    List<EntidadCuotaParteRespuesta> getEntidadesYCuotaParteByPensionadoId(Long pensionadoId);


}
