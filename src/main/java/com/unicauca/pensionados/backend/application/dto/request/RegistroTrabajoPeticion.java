package com.unicauca.pensionados.backend.application.dto.request;

import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistroTrabajoPeticion {
    private Long nitEntidad;
    private String nombreEntidad;
    private Long numeroIdentificacion; 
    private TipoIdentificacion tipoIdentificacion; 
    private Long diasDeServicio;
}
