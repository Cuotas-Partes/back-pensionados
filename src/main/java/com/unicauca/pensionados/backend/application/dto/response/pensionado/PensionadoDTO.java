package com.unicauca.pensionados.backend.application.dto.response.pensionado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.unicauca.pensionados.backend.application.dto.response.resolucion.ResolucionResponseDTO;
import com.unicauca.pensionados.backend.application.dto.response.sucesor.SucesorRespuesta;
import com.unicauca.pensionados.backend.domain.model.enums.*;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO de respuesta con información completa del pensionado")
public class PensionadoDTO {

    @Schema(description = "ID único del pensionado", example = "1")
    private Long idPensionado;

    @Schema(description = "Cédula del pensionado", example = "1061777777")
    private String cedula;

    @Schema(description = "Fecha de expedición de la cédula", example = "1995-06-10")
    private LocalDate fechaExpedicionCedula;

    @Schema(description = "Nombre del pensionado", example = "Juan")
    private String nombre;

    @Schema(description = "Apellidos del pensionado", example = "Pérez")
    private String apellidos;

    @Schema(description = "Fecha de nacimiento", example = "1960-03-15")
    private LocalDate fechaNacimiento;

    @Schema(description = "Teléfono de contacto", example = "3124567890")
    private String telefono;

    @Schema(description = "Correo electrónico", example = "juan.perez@email.com")
    private String correo;

    @Schema(description = "Nombre de la entidad de jubilación", example = "Universidad del Cauca")
    private String entidadJubilacion;

    @Schema(description = "ID de la entidad de jubilación", example = "1")
    private Long entityId;

    @Schema(description = "Días trabajados en la entidad", example = "8500")
    private Integer diasTrabajadosEntidad;

    @Schema(description = "Días totales trabajados", example = "12000")
    private Integer diasTotalesTrabajados;

    @Schema(description = "Porcentaje de cuota parte", example = "75.50")
    private BigDecimal porcentajeCuota;

    @Schema(description = "Tipo de jubilación", example = "VEJEZ")
    private TipoPension tipoJubilacion;

    @Schema(description = "Valor actual de la pensión", example = "3500000.00")
    private BigDecimal valorPensionActual;

    @Schema(description = "Estado del pensionado", example = "Activo")
    private EstadoPersona estado;

    @Schema(description = "Fecha de fallecimiento (si aplica)", example = "null")
    private LocalDate fechaFallecimiento;

    @Schema(description = "Indica si tiene sustitutos", example = "true")
    private Boolean tieneSustituto;

    @Schema(description = "Número de cuotas pendientes", example = "0")
    private Integer cuotasPendientes;

    @Schema(description = "Valor total pendiente", example = "0.00")
    private BigDecimal totalPendiente;

    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización")
    private LocalDateTime updatedAt;

    @Schema(description = "Lista de resoluciones asociadas")
    private List<ResolucionResponseDTO> resoluciones;

    @Schema(description = "Lista de sustitutos/sucesores")
    private List<SucesorRespuesta> sustitutos;
}

