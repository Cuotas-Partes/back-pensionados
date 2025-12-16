package com.unicauca.pensionados.backend.application.dto.response.pensionado;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;


import com.unicauca.pensionados.backend.application.dto.response.resolucion.ResolucionResponseDTO;
import com.unicauca.pensionados.backend.application.dto.response.sucesor.SucesorRespuesta;
import com.unicauca.pensionados.backend.domain.model.enums.*;

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
    private Long entityId;
    private Integer diasTrabajadosEntidad;
    private Integer diasTotalesTrabajados;
    private BigDecimal porcentajeCuota;
    private TipoPension tipoJubilacion;
    private BigDecimal valorPension;
    private EstadoPersona estado = EstadoPersona.Activo;
    private LocalDate fechaFallecimiento;
    private String pensionadoSustituido;
    private Boolean tieneSustituto = false;
    private Integer cuotasPendientes = 0;
    private BigDecimal totalPendiente = BigDecimal.ZERO;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private List<ResolucionResponseDTO> resoluciones;
    private List<SucesorRespuesta> sustitutos;

    public PensionadoDTO(Long idPersona, String cedula, LocalDate fechaExpedicionCedula, String nombre, String apellidos, LocalDate fechaNacimiento, String telefono, String correo, String entidadJubilacion, String entityNit, Long entityId, Integer diasTrabajadosEntidad, Integer diasTotalesTrabajados, BigDecimal porcentajeCuota, TipoPension tipoJubilacion, BigDecimal valorPension, EstadoPersona estado, LocalDate fechaFallecimiento, Boolean tieneSustituto, Integer cuotasPendientes, BigDecimal totalPendiente, LocalDateTime createdAt, LocalDateTime updatedAt, List<ResolucionResponseDTO> resoluciones, List<SucesorRespuesta> sustitutos) {
        this.idPersona = idPersona;
        this.cedula = cedula;
        this.fechaExpedicionCedula = fechaExpedicionCedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.entidadJubilacion = entidadJubilacion;
        this.entityNit = entityNit;
        this.entityId = entityId;
        this.diasTrabajadosEntidad = diasTrabajadosEntidad;
        this.diasTotalesTrabajados = diasTotalesTrabajados;
        this.porcentajeCuota = porcentajeCuota;
        this.tipoJubilacion = tipoJubilacion;
        this.valorPension = valorPension;
        this.estado = estado;
        this.fechaFallecimiento = fechaFallecimiento;
        this.tieneSustituto = tieneSustituto;
        this.cuotasPendientes = cuotasPendientes;
        this.totalPendiente = totalPendiente;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.resoluciones = resoluciones;
        this.sustitutos = sustitutos;
    }
}