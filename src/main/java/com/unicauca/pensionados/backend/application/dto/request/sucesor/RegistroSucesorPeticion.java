package com.unicauca.pensionados.backend.application.dto.request.sucesor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoSustituto;
import com.unicauca.pensionados.backend.application.dto.util.MultiDateDeserializer;

import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para registrar o actualizar un sustituto de pensionado")
public class RegistroSucesorPeticion {

    @Schema(description = "Número de documento de identidad del sustituto",
            example = "1098765432",
            required = true)
    @NotBlank(message = "El número de documento es obligatorio")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "El número de documento debe tener entre 6 y 15 dígitos")
    private Long numeroDocumento;

    @Schema(description = "Tipo del documento de identidad del sustituto",
            example = "CC",
            required = true)
    @NotBlank(message = "El tipo de documento es obligatorio")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "El tipo de documento debe tener entre 1 y 10 caracteres")
    private TipoIdentificacion tipoIdentificacion;

    @Schema(description = "Nombre completo del sustituto",
            example = "Gloria Patricia Rodríguez de López",
            required = true)
    @NotBlank(message = "El nombre completo es obligatorio")
    private String nombreCompleto;

    @Schema(description = "Número de teléfono del sustituto",
            example = "3209876543")
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    private String telefono;

    @Schema(description = "Estado del sustituto (ACTIVO, INACTIVO, FALLECIDO)",
            example = "ACTIVO",
            allowableValues = {"ACTIVO", "INACTIVO", "FALLECIDO"})
    private EstadoSustituto estado;

    @Schema(description = "Fecha de inicio de la sustitución",
            example = "2023-06-01",
            required = true)
    @NotNull(message = "La fecha de inicio es obligatoria")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de finalización de la sustitución (null si está activo)",
            example = "null")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaFin;

    @Schema(description = "Porcentaje de la pensión que le corresponde al sustituto",
            example = "50.00",
            required = true,
            minimum = "0.01",
            maximum = "100.00")
    @NotNull(message = "El porcentaje de pensión es obligatorio")
    @DecimalMin(value = "0.01", message = "El porcentaje debe ser mayor a 0")
    @DecimalMax(value = "100.00", message = "El porcentaje no puede ser mayor a 100")
    private BigDecimal porcentajePension;

    @Schema(description = "ID del pensionado al que sustituye",
            example = "1",
            required = true)
    @NotNull(message = "El ID del pensionado sustituido es obligatorio")
    private Long pensionadoSustituido;

    @Schema(description = "ID de la resolución que nombra al sustituto",
            example = "3",
            required = true)
    @NotNull(message = "El ID de la resolución de nombramiento es obligatorio")
    private Long resolucionNombramientoId;
}