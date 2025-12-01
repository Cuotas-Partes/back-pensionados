package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Deuda;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DeudaDTO;

import java.util.List;

public interface IDeudaServicio {

    DeudaDTO guardarDeuda(DeudaDTO deuda);
    DeudaDTO actualizarDeuda(DeudaDTO deuda);
    void eliminarDeuda(Long id);

    DeudaDTO obtenerDeudaPorId(Long id);
    List<DeudaDTO> obtenerDeudasPorTipoEstadoPersona(Deuda.TipoDeuda tipoDeuda, Deuda.EstadoDeuda estadoDeuda, Long idPersona);
    List<DeudaDTO> listarDeudas();

}
