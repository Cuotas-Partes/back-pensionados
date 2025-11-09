package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.EstadoPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.GeneroPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.TipoIdPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.Genero;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;


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

    @Schema(description = "ID único de la persona en el sistema", example = "102")
    private Long idPersona; // <-- 1. Nuevo campo ID

    @Schema(description = "Número de identificación de la persona sucesora", example = "123456789")

    private Long numeroIdentificacion; // <-- 2. Renombrado

    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private TipoIdentificacion tipoIdentificacion; // <-- 3. Cambiado a Enum (antes String tipoIdPersona)

    @Schema(description = "Nombres de la persona", example = "Maria")

    private String nombrePersona;

    @Schema(description = "Apellidos de la persona", example = "Gomez")
    private String apellidosPersona;

    @Schema(description = "Estado civil de la persona", example = "VIUDO")
    private String estadoCivil; // <-- 4. Nuevo campo

    @Schema(description = "Fecha de nacimiento de la persona", example = "1980-05-10")
    private LocalDate fechaNacimientoPersona;

    @Schema(description = "Fecha de expedición del documento de identidad", example = "2000-01-01")
    private LocalDate fechaExpedicionDocumentoIdPersona;


    @Schema(description = "Estado de la persona (ACTIVO, FALLECIDO, etc)", example = "ACTIVO")
    private EstadoPersona estadoPersona; // <-- 5. Cambiado a Enum (antes String)

    @Schema(description = "Género de la persona", example = "FEMENINO")
    private Genero generoPersona; // <-- 6. Cambiado a Enum (antes String)

    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "null")
    private LocalDate fechaDefuncionPersona;

    // --- Otros campos de Sucesión ---
    @Schema(description = "Fecha de inicio de la sucesión", example = "2024-06-01")
    private LocalDate fechaInicioSucesion;
    
    @Schema(description = "Porcentaje de la pensión que le corresponde al sucesor", example = "50.0")
    private Double porcentajePension;
}
