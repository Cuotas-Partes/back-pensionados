package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SMMLVDTORespuesta {
    private Long id;
    private Integer ano;
    private BigDecimal valor;
    private LocalDate vigenciaDesde;
    private LocalDate vigenciaHasta;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
