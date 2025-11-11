package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar un pensionado con sus cuotas parte y el valor total a cobrar")
public class ResultadoCobroPorEntidadDTO {
    @Schema(description = "Lista de entidades con sus cuotas parte y el valor total a cobrar")
    private List<EntidadCuotaParteRespuesta> entidades;
    @Schema(description = "Valor total a cobrar por todas las cuotas parte de las entidades", example = "1500000.00")
    private BigDecimal valorTotalACobrar;

    public ResultadoCobroPorEntidadDTO() {}

    public ResultadoCobroPorEntidadDTO(List<EntidadCuotaParteRespuesta> entidades, BigDecimal valorTotalACobrar) {
        this.entidades = entidades;
        this.valorTotalACobrar = valorTotalACobrar;
    }

    public List<EntidadCuotaParteRespuesta> getEntidades() {
        return entidades;
    }

    public void setEntidades(List<EntidadCuotaParteRespuesta> entidades) {
        this.entidades = entidades;
    }

    public BigDecimal getValorTotalACobrar() {
        return valorTotalACobrar;
    }

    public void setValorTotalACobrar(BigDecimal valorTotalACobrar) {
        this.valorTotalACobrar = valorTotalACobrar;
    }
}
