package com.unicauca.pensionados.backend.application.dto.request.dtf;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtfRequestDTO {
    private String periodo;
    private Double valor;
}
