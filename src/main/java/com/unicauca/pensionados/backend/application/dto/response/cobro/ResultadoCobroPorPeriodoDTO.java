package com.unicauca.pensionados.backend.application.dto.response.cobro;

import java.math.BigDecimal;
import java.util.List;

import com.unicauca.pensionados.backend.application.dto.response.pensionado.PensionadoConCuotaParteDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar un pensionado con sus cuotas parte y el valor total a cobrar")
public class ResultadoCobroPorPeriodoDTO {
    @Schema(description = "Lista de pensionados con sus cuotas parte y el valor total a cobrar")
    private List<PensionadoConCuotaParteDTO> pensionados;
    @Schema(description = "Valor total a cobrar por todas las cuotas parte de los pensionados", example = "1500000.00")
    private BigDecimal valorTotalACobrar;

    public ResultadoCobroPorPeriodoDTO() {}

    public ResultadoCobroPorPeriodoDTO(List<PensionadoConCuotaParteDTO> pensionados, BigDecimal valorTotalACobrar) {
        this.pensionados = pensionados;
        this.valorTotalACobrar = valorTotalACobrar;
    }

    public List<PensionadoConCuotaParteDTO> getPensionados() {
        return pensionados;
    }

    public void setPensionados(List<PensionadoConCuotaParteDTO> pensionados) {
        this.pensionados = pensionados;
    }

    public BigDecimal getValorTotalACobrar() {
        return valorTotalACobrar;
    }

    public void setValorTotalACobrar(BigDecimal valorTotalACobrar) {
        this.valorTotalACobrar = valorTotalACobrar;
    }
}