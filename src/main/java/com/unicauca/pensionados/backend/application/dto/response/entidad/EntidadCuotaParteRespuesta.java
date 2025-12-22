package com.unicauca.pensionados.backend.application.dto.response.entidad;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta que contiene información de una entidad y su cuota parte total")
public class EntidadCuotaParteRespuesta {
    @Schema(description = "NIT de la entidad", example = "1234567890")
    private Long nitEntidad;
    @Schema(description = "Nombre de la entidad", example = "Universidad del Cauca")
    private String nombreEntidad;
    @Schema(description = "Valor total de la cuota parte", example = "1500000.00")
    private BigDecimal valorCuotaParteTotal;

    // Constructor para la query JPQL que recibe String nit, String nombre, BigDecimal valor
    public EntidadCuotaParteRespuesta(String nit, String nombre, BigDecimal valor) {
        this.nitEntidad = nit != null ? Long.parseLong(nit) : null;
        this.nombreEntidad = nombre;
        this.valorCuotaParteTotal = valor;
    }
}
