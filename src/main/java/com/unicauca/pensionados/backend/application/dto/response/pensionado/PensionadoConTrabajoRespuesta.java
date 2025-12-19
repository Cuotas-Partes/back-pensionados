package com.unicauca.pensionados.backend.application.dto.response.pensionado;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO para representar un pensionado con información de un trabajo específico")
public class PensionadoConTrabajoRespuesta {
    @Schema(description = "ID único del pensionado", example = "101")
    private Long idPensionado;

    @Schema(description = "Cédula del pensionado", example = "1061777777")
    private String cedula;

    @Schema(description = "Nombre del pensionado", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del pensionado", example = "Pérez")
    private String apellidos;

    @Schema(description = "Días de servicio del trabajo asociado a la entidad", example = "3650")
    private Long diasDeServicio; 
}