package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO para representar un pensionado con información de un trabajo específico")
public class PensionadoConTrabajoRespuesta {
    @Schema(description = "Número de identificación de la persona", example = "1061777777")
    private Long numeroIdPersona;
    @Schema(description = "Nombre de la persona", example = "Juan")
    private String nombrePersona;
    @Schema(description = "Apellidos de la persona", example = "Perez")
    private String apellidosPersona;
    @Schema(description = "Días de servicio del trabajo asociado a la entidad", example = "3650")
    private Long diasDeServicio; // del trabajo asociado a la entidad
}