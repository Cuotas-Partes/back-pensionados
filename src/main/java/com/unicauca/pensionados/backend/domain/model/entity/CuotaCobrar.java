package com.unicauca.pensionados.backend.domain.model.entity;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaCobrar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuota_cobrar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuotaCobrar extends CuotaBase {

    @Column(name = "porcentaje_universidad", nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentajeUniversidad;

    @Column(name = "valor_paga_universidad", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorPagaUniversidad;

    @Column(name = "total_cobrar", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCobrar;

    @OneToMany(mappedBy = "cuotaCobrar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCuota> detallesCuotas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoCuotaCobrar estado;

    @PrePersist
    protected void onCreateCobrar() {
        super.onCreate();
        if (estado == null) estado = EstadoCuotaCobrar.PENDIENTE;
        if (porcentajeUniversidad == null) porcentajeUniversidad = BigDecimal.ZERO;
        if (valorPagaUniversidad == null) valorPagaUniversidad = BigDecimal.ZERO;
        if (totalCobrar == null) totalCobrar = BigDecimal.ZERO;
    }
}
