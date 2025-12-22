package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class ResolucionBaseRequestDTO {
    @NotBlank
    private String numeroResolucion;

    @NotNull
    private LocalDate fechaResolucion;

    @NotNull
    private BigDecimal valorResolucion;

    @NotNull
    private TipoResolucion tipoResolucion;

    private String observaciones;

    // Datos de acuerdo al tipo
    private Map<String, Object> datosEspecificos;
}
