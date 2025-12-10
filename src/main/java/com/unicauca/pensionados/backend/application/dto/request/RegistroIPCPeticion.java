package com.unicauca.pensionados.backend.application.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroIPCPeticion {
    private Integer fechaIPC;
    private BigDecimal valorIPC;
}
