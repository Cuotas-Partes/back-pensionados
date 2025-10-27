package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditarPeriodoPeticion {
    private Long idPeriodo;
    private Integer fechaIPC;
    private Date fechaInicioPeriodo;
    private Date fechaFinPeriodo;
    private Double cuotaParteTotalPeriodo;
    
}