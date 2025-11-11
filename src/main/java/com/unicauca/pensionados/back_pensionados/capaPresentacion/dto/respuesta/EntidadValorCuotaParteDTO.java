package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema; 
import java.math.BigDecimal;

@Schema(description = "DTO que representa una entidad y su valor a cobrar por cuota parte")
public class EntidadValorCuotaParteDTO {
    @Schema(description = "NIT de la entidad", example = "1234567890")
    private String nit;
    @Schema(description = "Nombre de la entidad", example = "Universidad del Cauca")
    private String nombre;
    @Schema(description = "Valor a cobrar por cuota parte", example = "1500000.00")
    private BigDecimal valorACobrar;

    /**
     * Constructor por defecto.
     */
    public EntidadValorCuotaParteDTO() {}

    /**
     * Constructor con parámetros.
     *
     * @param nit NIT de la entidad
     * @param nombre Nombre de la entidad
     * @param valorACobrar Valor a cobrar por cuota parte
     */
    public EntidadValorCuotaParteDTO(String nit, String nombre, BigDecimal valorACobrar) {
        this.nit = nit;
        this.nombre = nombre;
        this.valorACobrar = valorACobrar;
    }

    // Getters y Setters
    @Schema(description = "Obtiene el NIT de la entidad")
    public String getNit() {
        return nit;
    }

    @Schema(description = "Establece el NIT de la entidad")
    public void setNit(String nit) {
        this.nit = nit;
    }

    @Schema(description = "Obtiene el nombre de la entidad")
    public String getNombre() {
        return nombre;
    }

    @Schema(description = "Establece el nombre de la entidad")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Schema(description = "Obtiene el valor a cobrar por cuota parte")
    public BigDecimal getValorACobrar() {
        return valorACobrar;
    }

    @Schema(description = "Establece el valor a cobrar por cuota parte")
    public void setValorACobrar(BigDecimal valorACobrar) {
        this.valorACobrar = valorACobrar;
    }
}