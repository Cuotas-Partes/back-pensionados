package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.time.LocalDate;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoCivil;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.Genero;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.util.MultiDateDeserializer;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.GeneroPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.TipoIdPersona;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Petición para registrar un sucesor, incluye datos personales y de sucesión.")
public class RegistroSucesorPeticion {
    @Schema(description = "Número de identificación de la persona sucesora", example = "123456789")

    private Long numeroIdentificacion; // <-- 2. Renombrado (antes numeroIdPersona)

    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private TipoIdentificacion tipoIdentificacion; // <-- 3. Cambiado a Enum (antes String tipoIdPersona)

    @Schema(description = "Nombres de la persona", example = "Juan")
    private String nombrePersona;

    @Schema(description = "Apellidos de la persona", example = "Pérez")
    private String apellidosPersona;
    
    @Schema(description = "Estado civil de la persona", example = "SOLTERO")
    private EstadoCivil estadoCivil; // <-- 4. Nuevo campo de tipo Enum

    @Schema(description = "Fecha de nacimiento de la persona", example = "1980-05-10")


    @JsonDeserialize(using =  MultiDateDeserializer.class)
    private LocalDate fechaNacimientoPersona;

    @Schema(description = "Fecha de expedición del documento de identidad", example = "2000-01-01")
    @JsonDeserialize(using = MultiDateDeserializer.class)
    private LocalDate fechaExpedicionDocumentoIdPersona;

    @Schema(description = "Estado de la persona (ACTIVO, FALLECIDO, etc)", example = "ACTIVO")
    private EstadoPersona estadoPersona; // <-- 5. Cambiado a Enum (antes String)

    @Schema(description = "Género de la persona", example = "FEMENINO")
    private Genero generoPersona; // <-- 6. Cambiado a Enum (antes String)

    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "null")

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
