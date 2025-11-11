package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IPCRespuestaDTO {
    private Integer fechaIPC;
    private BigDecimal valorIPC;
}
