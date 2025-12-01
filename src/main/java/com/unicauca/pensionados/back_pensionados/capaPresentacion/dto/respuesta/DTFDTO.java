package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTFDTO {

    private Long idDtf;
    private Long mes;
    private Long anio;
    private Double valor;
    private String usuario;
    private String fechaRegistro;

}
