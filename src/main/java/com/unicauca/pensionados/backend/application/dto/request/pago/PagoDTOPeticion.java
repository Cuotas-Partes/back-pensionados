package com.unicauca.pensionados.backend.application.dto.request.pago;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.TipoPago;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PagoDTOPeticion {
    
    @NotNull(message = "El NIT de la entidad es obligatorio")
    private Long nitEntidad;

    private Long idPensionado; // Opcional

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @NotNull(message = "El valor pagado es obligatorio")
    @Positive(message = "El valor pagado debe ser positivo")
    private BigDecimal valorPagado;

    @NotNull(message = "El capital es obligatorio")
    @Positive(message = "El capital debe ser positivo")
    private BigDecimal capital;

    private Integer ipcInicialFecha; // Fecha del IPC inicial (formato YYYYMM)

    private Integer ipcFinalFecha; // Fecha del IPC final (formato YYYYMM)

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    private Boolean verificado = false;

    @NotNull(message = "El tipo de pago es obligatorio")
    private TipoPago tipoPago;

    private String observaciones;
}

