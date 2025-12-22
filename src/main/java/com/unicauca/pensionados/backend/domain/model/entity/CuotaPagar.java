package com.unicauca.pensionados.backend.domain.model.entity;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuota_pagar")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CuotaPagar extends CuotaBase {

    @Column(name = "total_pagar", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalPagar;

    @OneToMany(mappedBy = "cuotaPagar", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleCuota> detallesCuotas = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoCuota estado;

    @PrePersist
    protected void onCreatePagar() {
        super.onCreate();
        if (estado == null) estado = EstadoCuota.PENDIENTE;
        if (totalPagar == null) totalPagar = BigDecimal.ZERO;
    }
}
