package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.util.MultiDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para registrar un sucesor, incluye datos personales y de sucesión.")
public class RegistroSucesorPeticion {
    @Schema(description = "Número de identificación de la persona sucesora", example = "123456789")
    private Long numeroIdPersona;
    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private String tipoIdPersona;
    @Schema(description = "Nombres de la persona", example = "Juan")
    private String nombrePersona;
    @Schema(description = "Apellidos de la persona", example = "Pérez")
    private String apellidosPersona;
    @Schema(description = "Fecha de nacimiento de la persona", example = "1980-05-10")
    @JsonDeserialize(using =  MultiDateDeserializer.class)
    private LocalDate fechaNacimientoPersona;
    @Schema(description = "Fecha de expedición del documento de identidad", example = "2000-01-01")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaExpedicionDocumentoIdPersona;
    @Schema(description = "Estado de la persona (Activo/Inactivo)", example = "Activo")
    private String estadoPersona;
    @Schema(description = "Género de la persona", example = "M")
    private String generoPersona;
    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "2024-01-01")
    @JsonDeserialize(using =  MultiDateDeserializer.class)
    private LocalDate fechaDefuncionPersona;

    @Schema(description = "Fecha de inicio de la sucesión", example = "2024-06-01")
    @JsonDeserialize(using =  MultiDateDeserializer.class)
    LocalDate fechaInicioSucesion;
    @Schema(description = "ID del pensionado asociado al sucesor", example = "987654321")
    Long pensionado;
    @Schema(description = "Porcentaje de la pensión que le corresponde al sucesor", example = "50.0")
    Double porcentajePension;
}
