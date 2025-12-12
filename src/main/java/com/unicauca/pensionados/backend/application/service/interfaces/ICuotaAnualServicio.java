package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.response.CuotaAnualDTO;

import java.util.List;

public interface ICuotaAnualServicio {

    CuotaAnualDTO guardarCuotaAnual(CuotaAnualDTO cuotaAnual);
    CuotaAnualDTO actualizarCuotaAnual(CuotaAnualDTO cuotaAnual);
    void eliminarCuotaAnual(Long id);

    CuotaAnualDTO obtenerCuotaAnualPorAnio(Long ano);
    CuotaAnualDTO obtenerCuotaAnualPorId(Long id);

    List<CuotaAnualDTO> listarCuotasAnuales();


}
