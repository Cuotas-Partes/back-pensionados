package com.unicauca.pensionados.backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "detalle_cuota")
@Getter
@Setter
public class DetalleCuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuota_pagar_id")
    private CuotaPagar cuotaPagar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuota_cobrar_id")
    private CuotaCobrar cuotaCobrar;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "valor", nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }
    }
}
