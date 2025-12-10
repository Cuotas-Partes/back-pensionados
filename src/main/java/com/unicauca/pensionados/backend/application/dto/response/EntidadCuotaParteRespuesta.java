package com.unicauca.pensionados.backend.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Respuesta que contiene información de una entidad y su cuota parte total")
public class EntidadCuotaParteRespuesta {
    @Schema(description = "NIT de la entidad", example = "1234567890")
    private Long nitEntidad;
    @Schema(description = "Nombre de la entidad", example = "Universidad del Cauca")
    private String nombreEntidad;
    @Schema(description = "Valor total de la cuota parte", example = "1500000.00")
    private BigDecimal valorCuotaParteTotal;
}
