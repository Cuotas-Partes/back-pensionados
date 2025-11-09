package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;

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
