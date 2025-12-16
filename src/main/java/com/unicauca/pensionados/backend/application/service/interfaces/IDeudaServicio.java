package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

public interface IDeudaServicio {

    DeudaDTO crearDeuda(DeudaDTO deuda);
    DeudaDTO actualizarDeuda(DeudaDTO deuda);
    void eliminarDeuda(Long id);

    DeudaDTO obtenerDeudaPorId(Long id);
    List<DeudaDTO> obtenerDeudasPorTipoEstadoPersona(Deuda.TipoDeuda tipoDeuda, Deuda.EstadoDeuda estadoDeuda, Long idPersona);
    List<DeudaDTO> listarDeudas();

}
