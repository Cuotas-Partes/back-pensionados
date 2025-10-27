package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar un pensionado con sus cuotas partes asociadas")
public class PensionadoConCuotaParteDTO {
    @Schema(description = "Cédula del pensionado", example = "1061777777")
    private String cedula;
    @Schema(description = "Nombres del pensionado", example = "Juan")
    private String nombres;
    @Schema(description = "Apellidos del pensionado", example = "Perez")
    private String apellidos;
    @Schema(description = "Lista de cuotas partes asociadas al pensionado")
    private List<CuotaParteDTO> cuotasParte;
    @Schema(description = "Valor total del cobro de las cuotas partes", example = "2500000.50")
    private BigDecimal valorTotalCobro;

    public PensionadoConCuotaParteDTO() {}

    public PensionadoConCuotaParteDTO(String cedula, String nombres, String apellidos, List<CuotaParteDTO> cuotasParte, BigDecimal valorTotalCobro) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cuotasParte = cuotasParte;
        this.valorTotalCobro = valorTotalCobro;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public List<CuotaParteDTO> getCuotasParte() {
        return cuotasParte;
    }

    public void setCuotasParte(List<CuotaParteDTO> cuotasParte) {
        this.cuotasParte = cuotasParte;
    }

    public BigDecimal getValorTotalCobro() {
        return valorTotalCobro;
    }

    public void setValorTotalCobro(BigDecimal valorTotalCobro) {
        this.valorTotalCobro = valorTotalCobro;
    }
}