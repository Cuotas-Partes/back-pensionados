package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

@Schema(description = "Filtro para la consulta de cuota parte por entidad")
public class FiltroCuotaParteEntidadPeticion {

    @Schema(description = "Año para el cual se desea consultar la cuota parte", example = "2023")
    private Integer anio;
    @Schema(description = "Mes inicial del rango de consulta", example = "1")
    private Integer mesInicial;
    @Schema(description = "Mes final del rango de consulta", example = "12")
    private Integer mesFinal;
}
