package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;


import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.GeneroPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.TipoIdPersona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.Genero;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la respuesta de un pensionado con todos sus detalles")
public class PensionadoRespuesta {
    
    @Schema(description = "ID único de la persona en el sistema", example = "101")
    private Long idPersona; // <-- 1. Nuevo campo ID de la persona

    @Schema(description = "Número de identificación de la persona", example = "1061777777")

    private Long numeroIdentificacion; // <-- 2. Renombrado (antes numeroIdPersona)

    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private TipoIdentificacion tipoIdentificacion; // <-- 3. Renombrado (antes tipoIdPersona)

    @Schema(description = "Nombre de la persona", example = "Juan")
    private String nombrePersona;

    @Schema(description = "Apellidos de la persona", example = "Perez")
    private String apellidosPersona;
    
    @Schema(description = "Estado civil de la persona", example = "CASADO")
    private String estadoCivil; // <-- 4. Nuevo campo

    @Schema(description = "Fecha de nacimiento de la persona", example = "1950-01-15")

    private LocalDate fechaNacimientoPersona;

    @Schema(description = "Fecha de expedición del documento de identidad de la persona", example = "1970-01-15")
    private LocalDate fechaExpedicionDocumentoIdPersona;


    @Schema(description = "Estado de la persona (ACTIVO, FALLECIDO, etc)", example = "ACTIVO")
    private EstadoPersona estadoPersona; // <-- 5. Cambiado a Enum (antes String)

    @Schema(description = "Género de la persona", example = "FEMENINO")
    private Genero generoPersona; // <-- 6. Cambiado a Enum (antes String)

    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "null")

    private LocalDate fechaDefuncionPersona;

    // --- Otros campos de Pensionado ---
    @Schema(description = "Fecha de inicio de la pensión", example = "2010-05-20")
    private LocalDate fechaInicioPension;


    @Schema(description = "Valor inicial de la pensión", example = "1500000.00")
    private BigDecimal valorInicialPension;
    @Schema(description = "Resolución de la pensión", example = "RES-12345")
    private String resolucionPension;
    @Schema(description = "Nombre de la entidad de jubilación", example = "Colpensiones")
    private String entidadJubilacion;
    @Schema(description = "Total de días trabajados para la pensión", example = "7500")
    private Long totalDiasTrabajo;
    @Schema(description = "Días de servicio en la entidad de jubilación", example = "7500")
    private Long diasDeServicio;
    @Schema(description = "NIT de la entidad de jubilación", example = "800123456")
    private Long nitEntidad; 

    @Schema(description = "Lista de trabajos asociados al pensionado (Dependencias)")
    private List<TrabajoRespuesta> trabajos; // <-- 7. Este campo representa las 'Dependencias'

    @Schema(description = "Lista de sucesores del pensionado")
    private List<SucesorRespuesta> sucesores;

    @Schema(description = "Indica si se debe aplicar IPC al primer periodo", example = "false")
    private boolean aplicarIPCPrimerPeriodo;
}