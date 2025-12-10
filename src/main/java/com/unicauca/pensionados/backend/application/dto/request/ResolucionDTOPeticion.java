package com.unicauca.pensionados.backend.application.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResolucionDTOPeticion {
    
    @NotBlank(message = "El número de resolución es obligatorio")
    private String numeroResolucion;

    @NotNull(message = "La fecha de resolución es obligatoria")
    private LocalDate fechaResolucion;

    @NotNull(message = "El valor de resolución es obligatorio")
    @Positive(message = "El valor debe ser positivo")
    private BigDecimal valorResolucion;

    private EstadoResolucion estado = EstadoResolucion.VIGENTE;

    @NotNull(message = "El tipo de resolución es obligatorio")
    private TipoResolucion tipoResolucion;

    @NotNull(message = "El ID del pensionado es obligatorio")
    private Long pensionadoId;
}
