package com.unicauca.pensionados.backend.application.dto.request.pensionado;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unicauca.pensionados.backend.application.dto.util.MultiDateDeserializer;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición anidada para registrar/actualizar una resolución de un pensionado")
public class RegistroResolucionPensionadoPeticion {

    @Schema(description = "Número de resolución", example = "RES-12345")
    @NotBlank(message = "El número de resolución es obligatorio")
    private String numeroResolucion;

    @Schema(description = "Fecha de resolución", example = "2010-05-20")
    @NotNull(message = "La fecha de resolución es obligatoria")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaResolucion;

    @Schema(description = "Valor de resolución", example = "1500000.00")
    @NotNull(message = "El valor de resolución es obligatorio")
    private BigDecimal valorResolucion;

    @Schema(description = "Estado de resolución", example = "VIGENTE")
    private EstadoResolucion estado;

    @Schema(description = "Tipo de resolución", example = "PENSION")
    @NotNull(message = "El tipo de resolución es obligatorio")
    private TipoResolucion tipoResolucion;
}
