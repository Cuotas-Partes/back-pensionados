package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistroIPCPeticion {
    private Integer fechaIPC;
    private BigDecimal valorIPC;
}
