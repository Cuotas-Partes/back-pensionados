package com.unicauca.pensionados.backend.domain.model.entity;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoPeriodo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "periodo")
@Getter
@Setter
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_periodo")
    private Long idPeriodo;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "fecha_inicio_periodo", nullable = false)
    private LocalDate fechaInicioPeriodo;

    @Column(name = "fecha_fin_periodo", nullable = false)
    private LocalDate fechaFinPeriodo;

    @Column(name = "ipc", precision = 10, scale = 4)
    private BigDecimal ipc;

    @Column(name = "cuota_parte_total_periodo", precision = 19, scale = 2)
    private BigDecimal cuotaParteTotalPeriodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_periodo", nullable = false, length = 20)
    private EstadoPeriodo estadoPeriodo = EstadoPeriodo.ACTIVO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (cuotaParteTotalPeriodo == null) {
            cuotaParteTotalPeriodo = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
