package com.unicauca.pensionados.backend.application.dto.response.pensionado;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para representar un pensionado con sus cuotas partes asociadas")
public class PensionadoConCuotaParteDTO {

    @Schema(description = "Cédula del pensionado", example = "1061777777")
    private String cedula;

    @Schema(description = "Nombre del pensionado", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del pensionado", example = "Pérez")
    private String apellidos;

    @Schema(description = "Valor total del cobro de las cuotas partes", example = "2500000.50")
    private BigDecimal valorTotalCobro;
}