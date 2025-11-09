package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para representar el resultado del cobro por pensionado")
public class ResultadoCobroPorPensionado {
    @Schema(description = "Lista de pensionados con sus cuotas parte y el valor total a cobrar")
    private List<PensionadoConCuotaParteDTO> pensionados;
    @Schema(description = "Valor total a cobrar por todas las cuotas parte de los pensionados", example = "1500000.00")
    private BigDecimal valorTotalACobrar;
}
