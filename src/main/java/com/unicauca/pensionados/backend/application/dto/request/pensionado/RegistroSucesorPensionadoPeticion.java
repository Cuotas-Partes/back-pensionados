package com.unicauca.pensionados.backend.application.dto.request.pensionado;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unicauca.pensionados.backend.application.dto.util.MultiDateDeserializer;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCivil;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoSustituto;
import com.unicauca.pensionados.backend.domain.model.enums.Genero;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Schema(description = "Petición anidada para registrar/actualizar un sucesor (sustituto) del pensionado")
public class RegistroSucesorPensionadoPeticion {

    @Schema(description = "Número de identificación del sucesor", example = "1098765432")
    @NotNull(message = "El número de identificación es obligatorio")
    private Long numeroIdentificacion;

    @Schema(description = "Tipo de identificación", example = "CC")
    @NotNull(message = "El tipo de identificación es obligatorio")
    private TipoIdentificacion tipoIdentificacion;

    @Schema(description = "Nombre completo", example = "Gloria Patricia ")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200)
    private String nombrePersona;

    @Schema(description = "Apellido completo", example = "Rodríguez")
    @NotBlank(message = "El Apellido es obligatorio")
    @Size(max = 200)
    private String apellidoPersona;

    @Schema(description = "Estado", example = "Activo")
    private EstadoSustituto estado;

    @Schema(description = "Estado Civil", example = "VIUDO")
    private EstadoCivil estadoCivil = EstadoCivil.VIUDO;


    @Schema(description = "Género", example = "FEMENINO")
    private Genero generoPersona;


    @Schema(description = "Fecha inicio", example = "2023-06-01")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaInicio;

    @Schema(description = "Fecha fin (null si activo)", example = "null")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaFin;

    @Schema(description = "Porcentaje pensión", example = "50.00")
    @NotNull(message = "El porcentaje de pensión es obligatorio")
    @DecimalMin(value = "0.01")
    @DecimalMax(value = "100.00")
    private BigDecimal porcentajePension;

    @Schema(description = "Número de resolución que nombra al sucesor (opcional)", example = "RES-999")
    private String numeroResolucionNombramiento;
}
