package com.unicauca.pensionados.backend.application.dto.response.ipc;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoIPC;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPCRespuestaDTO {

    private Long id;
    private Integer year;
    private BigDecimal ipc;
    private String resolution;
    private LocalDate resolutionDate;
    private String resolutionDetails;
    private EstadoIPC estado;
}
