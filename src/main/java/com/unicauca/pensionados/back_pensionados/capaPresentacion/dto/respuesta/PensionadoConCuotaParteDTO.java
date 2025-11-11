package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para representar un pensionado con sus cuotas partes asociadas")
public class PensionadoConCuotaParteDTO {

    // <-- CAMBIOS: Se reemplaza 'cedula' por los nuevos campos de identificación.
    @Schema(description = "Tipo de identificación", example = "CC")
    private String tipoIdentificacion;
    @Schema(description = "Número de identificación del pensionado", example = "1061777777")
    private Long numeroIdentificacion;

    @Schema(description = "Nombres del pensionado", example = "Juan")
    private String nombres;
    @Schema(description = "Apellidos del pensionado", example = "Perez")
    private String apellidos;
    @Schema(description = "Lista de cuotas partes asociadas al pensionado")
    private List<CuotaParteDTO> cuotasParte;
    @Schema(description = "Valor total del cobro de las cuotas partes", example = "2500000.50")
    private BigDecimal valorTotalCobro;

    // Constructores y Getters/Setters actualizados
    
    public PensionadoConCuotaParteDTO() {}

    // Constructor actualizado
    public PensionadoConCuotaParteDTO(String tipoIdentificacion, Long numeroIdentificacion, String nombres, String apellidos, List<CuotaParteDTO> cuotasParte, BigDecimal valorTotalCobro) {
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cuotasParte = cuotasParte;
        this.valorTotalCobro = valorTotalCobro;
    }

    // Getters y Setters para los nuevos campos
    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    public Long getNumeroIdentificacion() { return numeroIdentificacion; }
    public void setNumeroIdentificacion(Long numeroIdentificacion) { this.numeroIdentificacion = numeroIdentificacion; }

    // (los demás getters y setters se mantienen igual)
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public List<CuotaParteDTO> getCuotasParte() { return cuotasParte; }
    public void setCuotasParte(List<CuotaParteDTO> cuotasParte) { this.cuotasParte = cuotasParte; }
    public BigDecimal getValorTotalCobro() { return valorTotalCobro; }
    public void setValorTotalCobro(BigDecimal valorTotalCobro) { this.valorTotalCobro = valorTotalCobro; }
}