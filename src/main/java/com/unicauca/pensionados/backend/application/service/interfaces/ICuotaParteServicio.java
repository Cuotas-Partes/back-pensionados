package com.unicauca.pensionados.backend.application.service.interfaces;

import org.springframework.stereotype.Service;

import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.application.dto.response.cobro.ResultadoCobroPorPensionado;

@Service
public interface ICuotaParteServicio {
    void registrarCuotaParte (Trabajo trabajo);
    void actualizarCuotaParte(Trabajo trabajo);
    void recalcularCuotasPartesPorPensionado(Pensionado pensionado);
    ResultadoCobroPorPensionado cuotasPartesPorCobrarPensionado();
}