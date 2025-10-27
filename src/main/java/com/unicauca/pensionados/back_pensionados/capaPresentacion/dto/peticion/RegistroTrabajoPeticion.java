package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

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
    private Long numeroIdPersona;
    private Long diasDeServicio;
}
