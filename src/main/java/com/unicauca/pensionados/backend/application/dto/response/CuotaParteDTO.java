package com.unicauca.pensionados.backend.application.dto.response;

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

    @Schema(description = "Fecha de la última actualización de la cuota parte", example = "2023-11-15")
    private LocalDate fechaActualizacion;

    @Schema(description = "Usuario que realizó la última actualización", example = "CarlosT")
    private String usuarioActualizacion;

    @Schema(description = "Observaciones sobre la actualización", example = "Recálculo por ajuste de IPC.")
    private String observaciones;

    public CuotaParteDTO() {}

    public CuotaParteDTO(Long idCuotaParte, BigDecimal valorCuotaParte, LocalDate fechaGeneracion) {
        this.idCuotaParte = idCuotaParte;
        this.valorCuotaParte = valorCuotaParte;
        this.fechaGeneracion = fechaGeneracion;
    }

    // Constructor extendido con nuevos campos
    public CuotaParteDTO(Long idCuotaParte, BigDecimal valorCuotaParte, LocalDate fechaGeneracion,
                         LocalDate fechaActualizacion, String usuarioActualizacion, String observaciones) {
        this.idCuotaParte = idCuotaParte;
        this.valorCuotaParte = valorCuotaParte;
        this.fechaGeneracion = fechaGeneracion;
        this.fechaActualizacion = fechaActualizacion;
        this.usuarioActualizacion = usuarioActualizacion;
        this.observaciones = observaciones;
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

     public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(String usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

}