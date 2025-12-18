package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaParte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que representa las cuotas partes que la entidad principal (Universidad del Cauca)
 * debe pagar a otras entidades por pensionados que trabajaron en múltiples instituciones.
 */
@Entity
@Table(name = "cuota_parte_por_pagar", indexes = {
    @Index(name = "idx_cuota_pagar_pensionado", columnList = "idPensionado"),
    @Index(name = "idx_cuota_pagar_entidad_acreedora", columnList = "idEntidadAcreedora"),
    @Index(name = "idx_cuota_pagar_periodo", columnList = "anioPeriodo, mesPeriodo"),
    @Index(name = "idx_cuota_pagar_estado", columnList = "estado")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CuotaPartePorPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuotaParte")
    private Long idCuotaParte;

    // Relación con el pensionado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPensionado", nullable = false)
    private Pensionado pensionado;

    // Información del pensionado (desnormalizada para consultas rápidas)
    @Column(name = "cedulaPensionado", nullable = false, length = 20)
    private String cedulaPensionado;

    @Column(name = "nombrePensionado", nullable = false, length = 400)
    private String nombrePensionado;

    // Entidad que recibe el pago (acreedora)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEntidadAcreedora", nullable = false)
    private Entidad entidadAcreedora;

    // Información de la entidad acreedora (desnormalizada)
    @Column(name = "nitEntidadAcreedora", nullable = false, length = 20)
    private String nitEntidadAcreedora;

    @Column(name = "nombreEntidadAcreedora", nullable = false, length = 200)
    private String nombreEntidadAcreedora;

    // Periodo de liquidación
    @Column(name = "anioPeriodo", nullable = false)
    private Integer anioPeriodo;

    @Column(name = "mesPeriodo", nullable = false)
    private Integer mesPeriodo;

    @Column(name = "periodoDescripcion", length = 50)
    private String periodoDescripcion;

    // Relaciones con valores históricos para cálculo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idDtf")
    private DTF dtfAplicado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idIpc")
    private IPC ipcAplicado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSmmlv")
    private SMMLVHistorico smmlvAplicado;

    // Valores históricos almacenados (desnormalizados para registro)
    @Column(name = "valorDtfUtilizado", precision = 10, scale = 4)
    private BigDecimal valorDtfUtilizado;

    @Column(name = "valorIpcUtilizado", precision = 10, scale = 4)
    private BigDecimal valorIpcUtilizado;

    @Column(name = "valorSmmlvUtilizado", precision = 19, scale = 2)
    private BigDecimal valorSmmlvUtilizado;

    // Valores económicos base
    @Column(name = "valorPensionBase", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPensionBase; // Valor inicial de la pensión

    @Column(name = "valorPensionAjustado", precision = 19, scale = 2)
    private BigDecimal valorPensionAjustado; // Valor ajustado por IPC/DTF

    @Column(name = "valorPensionPeriodo", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPensionPeriodo; // Valor final para el periodo

    @Column(name = "porcentajeCuotaParte", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeCuotaParte;

    @Column(name = "valorCuotaParte", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorCuotaParte;

    @Column(name = "diasTrabajadosEntidadPagadora", nullable = false)
    private Integer diasTrabajadosEntidadPagadora;

    @Column(name = "diasTotalesTrabajados", nullable = false)
    private Integer diasTotalesTrabajados;

    // Información adicional de cálculo
    @Column(name = "mesadasPagadas")
    private Integer mesadasPagadas; // Número de mesadas pagadas en el periodo

    @Column(name = "factorAjuste", precision = 10, scale = 6)
    private BigDecimal factorAjuste; // Factor de ajuste aplicado (IPC o DTF)

    @Column(name = "detalleCalculo", columnDefinition = "TEXT")
    private String detalleCalculo; // JSON o texto con detalle del cálculo realizado

    // Estado y seguimiento
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoCuotaParte estado = EstadoCuotaParte.PENDIENTE;

    @Column(name = "fechaGeneracion", nullable = false)
    private LocalDate fechaGeneracion;

    @Column(name = "fechaVencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "fechaPago")
    private LocalDate fechaPago;

    // Información de resolución si aplica
    @Column(name = "numeroResolucion", length = 100)
    private String numeroResolucion;

    @Column(name = "fechaResolucion")
    private LocalDate fechaResolucion;

    // Información bancaria de la entidad acreedora
    @Column(name = "nombreBanco", length = 200)
    private String nombreBanco;

    @Column(name = "numeroCuenta", length = 100)
    private String numeroCuenta;

    @Column(name = "tipoCuenta", length = 50)
    private String tipoCuenta;

    // Observaciones
    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    // Auditoría
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "createdBy", length = 100)
    private String createdBy;

    @Column(name = "updatedBy", length = 100)
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.fechaGeneracion == null) {
            this.fechaGeneracion = LocalDate.now();
        }
        if (this.estado == null) {
            this.estado = EstadoCuotaParte.PENDIENTE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

