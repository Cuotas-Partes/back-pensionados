package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.RegistroPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.PensionadoRespuesta;

import java.util.List;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;

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
