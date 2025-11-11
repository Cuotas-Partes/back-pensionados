package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuotaAnualDTO {
    private Long idCuotaAnual;
    private Long anio;
    private Double valorIpc;
    private String usuarioRegistra;
    private Double salarioMinimoVigente;
    private String fechaRegistro;
    private Double UVT;
    private Double tasaInteresMoraAnual;
    private Double pIncrementoPensionalAnual;
}
