package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResolucionDTOPeticion {

    private Long pensionadoId;
    private String numeroResolucion;
    private LocalDate fechaResolucion;
    private TipoResolucion tipoResolucion;
    private BigDecimal valorResolucion; // Opcional según tipo
    private String observaciones;
    private EstadoResolucion estado;


    // Datos de acuerdo al tipo
    private Map<String, Object> datosEspecificos;
}
