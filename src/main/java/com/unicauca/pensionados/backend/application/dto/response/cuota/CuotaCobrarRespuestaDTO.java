package com.unicauca.pensionados.backend.application.dto.response.cuota;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuotaCobrar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CuotaCobrarRespuestaDTO {

    private Long id;

    private Long pensionadoId;
    private Long entidadId;
    private Long periodoId;

    private LocalDate fechaInicio;
    private LocalDate fechaLiquidacion;
    private Integer diasTotales;
    private Integer cantidadCuotas;

    private BigDecimal tasaDiaria;
    private BigDecimal valorCuotaParte;
    private BigDecimal valorCuotasTotal;
    private BigDecimal valorInteresTotal;
    private BigDecimal ajuste;

    private Boolean esReliquidacion;
    private String comentarios;

    private BigDecimal porcentajeUniversidad;
    private BigDecimal valorPagaUniversidad;
    private BigDecimal totalCobrar;
    private EstadoCuotaCobrar estado;

    private List<DetalleCuotaRespuestaDTO> detallesCuotas;
}
