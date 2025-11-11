package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.enums.EstadoEntidad;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Respuesta que contiene información de una entidad junto con sus pensionados y trabajos asociados")
public class EntidadConPensionadosRespuesta {
    @Schema(description = "NIT de la entidad", example = "1234567890")
    private Long nitEntidad;
    @Schema(description = "Nombre de la entidad", example = "Universidad del Cauca")
    private String nombreEntidad;
    @Schema(description = "Dirección de la entidad", example = "Calle 5 # 4-70")
    private String direccionEntidad;
    @Schema(description = "Teléfono de la entidad", example = "123456789")
    private Long telefonoEntidad;
    @Schema(description = "Email de la entidad", example = "relacionesinter@unicauca.edu.co")
    private String emailEntidad;
    @Schema(description = "Estado de la entidad", example = "ACTIVO")

    private EstadoEntidad estadoEntidad;

    @Schema(description = "Lista de pensionados asociados a la entidad")
    private List<PensionadoRespuesta> pensionados;
    @Schema(description = "Lista de trabajos asociados a la entidad")
    private List<TrabajoRespuesta>trabajos;
}