package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.entity.Deuda;
import com.unicauca.pensionados.backend.domain.model.entity.Persona;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeudaDTO {

    private Long idDeuda;
    private Deuda.TipoDeuda tipoDeuda;
    private Deuda.EstadoDeuda estadoDeuda;
    private Persona persona;
    // private Pensionado pensionado;
    private Double montoDeuda;
    private String fechaCreacion = java.time.LocalDate.now().toString();
    private String fechaVencimiento;
    private Double tasaInteresAplicada;
    private String usuarioRegistro;

}
