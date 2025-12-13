package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.unicauca.pensionados.backend.domain.model.entity.Resolucion;
import com.unicauca.pensionados.backend.domain.model.enums.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PensionadoDTO{

    private Long idPersona;
    private String cedula;
    private LocalDate fechaExpedicionCedula;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;
    private String entidadJubilacion;
    private String entityNit;
    private String entityId;
    private Integer diasTrabajadosEntidad;
    private Integer diasTotalesTrabajados;
    private BigDecimal porcentajeCuota;
    private TipoPension tipoJubilacion;
    private BigDecimal valorPension;
    private EstadoPersona estado = EstadoPersona.Activo;
    private LocalDate fechaFallecimiento;
    private String pensionadoSustituido;
    private Boolean tieneSustituto = false;
    private Long sustitutoId;
    private Integer cuotasPendientes = 0;
    private BigDecimal totalPendiente = BigDecimal.ZERO;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private List<ResolucionDTORespuesta> resoluciones;
}