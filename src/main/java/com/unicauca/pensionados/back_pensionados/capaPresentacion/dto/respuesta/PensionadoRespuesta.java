package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

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
    
    @Schema(description = "Número de identificación de la persona", example = "1061777777")
    private Long numeroIdPersona;
    @Schema(description = "Tipo de identificación de la persona", example = "CC")
    private String tipoIdPersona;
    @Schema(description = "Nombre de la persona", example = "Juan")
    private String nombrePersona;
    @Schema(description = "Apellidos de la persona", example = "Perez")
    private String apellidosPersona;
    @Schema(description = "Fecha de nacimiento de la persona", example = "1950-01-15")
    private LocalDate fechaNacimientoPersona;
    @Schema(description = "Fecha de expedición del documento de identidad de la persona", example = "1970-01-15")
    private LocalDate fechaExpedicionDocumentoIdPersona;
    @Schema(description = "Estado de la persona (Activo, Fallecido)", example = "Activo")
    private String estadoPersona;
    @Schema(description = "Género de la persona", example = "Masculino")
    private String generoPersona;
    @Schema(description = "Fecha de defunción de la persona (si aplica)", example = "null")
    private LocalDate fechaDefuncionPersona;
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
    @Schema(description = "Lista de trabajos asociados al pensionado")
    private List<TrabajoRespuesta> trabajos;
    @Schema(description = "Lista de sucesores del pensionado")
    private List<SucesorRespuesta> sucesores;
    @Schema(description = "Indica si se debe aplicar IPC al primer periodo", example = "false")
    private boolean aplicarIPCPrimerPeriodo;
    
}