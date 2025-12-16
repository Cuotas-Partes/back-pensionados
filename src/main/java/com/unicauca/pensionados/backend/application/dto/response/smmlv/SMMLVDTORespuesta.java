package com.unicauca.pensionados.backend.application.dto.response.smmlv;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SMMLVDTORespuesta {
    private Long id;
    private Integer ano;
    private BigDecimal valor;
    private boolean estado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
