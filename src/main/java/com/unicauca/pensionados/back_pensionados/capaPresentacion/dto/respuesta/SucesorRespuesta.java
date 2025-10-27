package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los datos de un sucesor, incluye información personal y de sucesión.")
public class SucesorRespuesta {
    @Schema(description = "Número de identificación de la persona sucesora", example = "123456789")
    private Long numeroIdPersona;
    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private String tipoIdPersona;
    @Schema(description = "Nombres de la persona", example = "Juan")
    private String nombrePersona;
    @Schema(description = "Apellidos de la persona", example = "Pérez")
    private String apellidosPersona;
    @Schema(description = "Fecha de nacimiento de la persona", example = "1980-05-10")
    private LocalDate fechaNacimientoPersona;
    @Schema(description = "Fecha de expedición del documento de identidad", example = "2000-01-01")
    private LocalDate fechaExpedicionDocumentoIdPersona;
    @Schema(description = "Estado de la persona (Activo/Inactivo)", example = "Activo")
    private String estadoPersona;
    @Schema(description = "Género de la persona", example = "M")
    private String generoPersona;
    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "2024-01-01")
    private LocalDate fechaDefuncionPersona;

    @Schema(description = "Fecha de inicio de la sucesión", example = "2024-06-01")
    private LocalDate fechaInicioSucesion;
    @Schema(description = "Porcentaje de la pensión que le corresponde al sucesor", example = "50.0")
    private Double porcentajePension;
}