package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar una cuota parte generada para un pensionado")
public class CuotaParteDTO {

    @Schema(description = "Identificador único de la cuota parte", example = "1")
    private Long idCuotaParte;
    @Schema(description = "Valor de la cuota parte generada", example = "150000.00")
    private BigDecimal valorCuotaParte;
    @Schema(description = "Fecha de generación de la cuota parte", example = "2023-10-01")
    private LocalDate fechaGeneracion;

    public CuotaParteDTO() {}

    public CuotaParteDTO(Long idCuotaParte, BigDecimal valorCuotaParte, LocalDate fechaGeneracion) {
        this.idCuotaParte = idCuotaParte;
        this.valorCuotaParte = valorCuotaParte;
        this.fechaGeneracion = fechaGeneracion;
    }

    public Long getIdCuotaParte() {
        return idCuotaParte;
    }

    public void setIdCuotaParte(Long idCuotaParte) {
        this.idCuotaParte = idCuotaParte;
    }

    public BigDecimal getValorCuotaParte() {
        return valorCuotaParte;
    }

    public void setValorCuotaParte(BigDecimal valorCuotaParte) {
        this.valorCuotaParte = valorCuotaParte;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}