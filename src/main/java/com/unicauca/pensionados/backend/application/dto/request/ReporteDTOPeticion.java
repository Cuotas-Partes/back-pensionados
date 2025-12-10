package com.unicauca.pensionados.backend.application.dto.request;

import com.unicauca.pensionados.backend.domain.model.enums.TipoReporte;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTOPeticion {
    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotNull(message = "El tipo de reporte es obligatorio")
    private TipoReporte tipo;

    private String filtros; // JSON
    private String datos; // JSON
    private String generadoPor;
}
