package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPCRespuestaDTO {
    private Integer fechaIPC;
    private BigDecimal valorIPC;
}
