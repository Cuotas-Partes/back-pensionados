package com.unicauca.pensionados.backend.application.dto.request.cuota;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoCuota;
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
public class RegistroCuotaPagarPeticion {

    private Long pensionadoId;
    private Long entidadId;
    private Long periodoId;

    private LocalDate fechaInicio;
    private LocalDate fechaLiquidacion;
    private Integer diasTotales;
    private Integer cantidadCuotas;

    private BigDecimal valorCuotaParte;  // Opcional - se calcula automáticamente si no se proporciona
    private BigDecimal ajuste;

    private Boolean esReliquidacion;
    private String comentarios;

    private BigDecimal totalPagar;
    private EstadoCuota estado;

    private List<DetalleCuotaPeticion> detallesCuotas;
}
