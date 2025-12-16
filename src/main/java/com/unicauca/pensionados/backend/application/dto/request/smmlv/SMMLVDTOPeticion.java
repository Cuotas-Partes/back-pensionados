package com.unicauca.pensionados.backend.application.dto.request.smmlv;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Min;
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
public class SMMLVDTOPeticion {

    private Integer ano;
    private BigDecimal valor;
}
