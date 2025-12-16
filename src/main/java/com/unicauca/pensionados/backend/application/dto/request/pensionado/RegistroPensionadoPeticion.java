package com.unicauca.pensionados.backend.application.dto.request.pensionado;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unicauca.pensionados.backend.application.dto.util.MultiDateDeserializer;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPension;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para crear/actualizar un pensionado (incluye resoluciones y sustitutos)")
public class RegistroPensionadoPeticion {

    @Schema(description = "Cédula del pensionado", example = "1061777777")
    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 20, message = "La cédula no puede exceder 20 caracteres")
    private String cedula;

    @Schema(description = "Fecha de expedición de la cédula", example = "1970-01-15")
    @NotNull(message = "La fecha de expedición de la cédula es obligatoria")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaExpedicionCedula;

    @Schema(description = "Nombre", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    private String nombre;

    @Schema(description = "Apellidos", example = "Pérez")
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 200)
    private String apellidos;

    @Schema(description = "Fecha de nacimiento", example = "1950-01-15")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaNacimiento;

    @Schema(description = "Teléfono", example = "3209876543")
    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20)
    private String telefono;

    @Schema(description = "Correo", example = "juan.perez@email.com")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 100)
    private String correo;

    @Schema(description = "Id de la entidad de jubilación", example = "1")
    @NotBlank(message = "El id de la entidad de jubilación es obligatorio")
    @Size(max = 20)
    private Long entityId;

    @Schema(description = "Días trabajados en la entidad de jubilación", example = "7500")
    @NotNull(message = "Los días trabajados en la entidad son obligatorios")
    private Integer diasTrabajadosEntidad;

    @Schema(description = "Días totales trabajados", example = "7500")
    @NotNull(message = "Los días totales trabajados son obligatorios")
    private Integer diasTotalesTrabajados;

    @Schema(description = "Tipo de jubilación", example = "VEJEZ")
    @NotNull(message = "El tipo de jubilación es obligatorio")
    private TipoPension tipoJubilacion;


    @Schema(description = "Estado del pensionado", example = "Activo")
    private EstadoPersona estado;

    @Schema(description = "Fecha de fallecimiento (si aplica)", example = "null")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaFallecimiento;

    @NotEmpty(message = "Debe haber al menos una resolución para el pensionado")
    @Schema(description = "Resoluciones del pensionado")
    private List<@Valid RegistroResolucionPensionadoPeticion> resoluciones;

    @Schema(description = "Sustitutos (sucesores) del pensionado")
    private List<@Valid RegistroSucesorPensionadoPeticion> sustitutos;

    @Schema(description = "Valor actual de la pensión", example = "1500000.00")
    private float valorPensionActual;


}
