package com.unicauca.pensionados.backend.application.dto.request.ipc;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistroIPCPeticion {
    private Integer year;
    private BigDecimal ipc;
    private String resolution;
    private LocalDate resolutionDate;
    private String resolutionDetails;
}
