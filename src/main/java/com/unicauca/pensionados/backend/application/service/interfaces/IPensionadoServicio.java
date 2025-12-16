package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.pensionado.RegistroPensionadoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.pensionado.PensionadoDTO;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;

import java.util.List;

public interface IPensionadoServicio {
    void registrarPensionado(RegistroPensionadoPeticion request);
    void actualizarPensionado(Long id, RegistroPensionadoPeticion request);
    void eliminarPensionado(Long id);

    List<Pensionado> listarPensionados();
    List<PensionadoDTO> listarPensionadoPorEntidad(Long entidadId);
    List<Pensionado> buscarPensionadosPorNombre(String nombre);
    List<Pensionado> buscarPensionadosPorApellido(String apellido);
    List<Pensionado> buscarPensionadosPorCriterio(String query);
    Pensionado buscarPensionadoPorId(Long id);
    void desactivarPensionado(Long id);

}
