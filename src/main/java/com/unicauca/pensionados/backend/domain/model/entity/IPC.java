package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoIPC;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "ipc")
@Getter @Setter
public class IPC {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "ipc", nullable = false, precision = 10, scale = 4)
    private BigDecimal ipc;

    @Column(name = "resolution", nullable = false, length = 100)
    private String resolution;

    @Column(name = "resolution_date", nullable = false)
    private LocalDate resolutionDate;

    @Enumerated(EnumType.STRING)
    @Column(name="estado", nullable = false, length = 20)
    private EstadoIPC estado = EstadoIPC.ACTIVO;

    @Column(name = "resolution_details", columnDefinition = "TEXT")
    private String resolutionDetails;

    @Column(name = "createdAt", nullable = false)
    private LocalDate createdAt = LocalDate.now();

    @Column(name = "updatedAt")
    private LocalDate updatedAt;

}
