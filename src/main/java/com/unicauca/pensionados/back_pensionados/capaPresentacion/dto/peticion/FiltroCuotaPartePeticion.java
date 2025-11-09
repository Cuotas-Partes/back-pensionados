package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Filtro para las solicitudes de cuota parte por año y mes")
public class FiltroCuotaPartePeticion {
    @Schema(description = "Año para filtrar las solicitudes de cuota parte", example = "2023")
    private Integer anio;
    @Schema(description = "Mes inicial para filtrar las solicitudes de cuota parte", example = "1")
    private Integer mesInicial;
    @Schema(description = "Mes final para filtrar las solicitudes de cuota parte", example = "12")
    private Integer mesFinal;
}