package com.unicauca.pensionados.backend.application.dto.request;

import java.time.LocalDate;

import com.unicauca.pensionados.backend.domain.model.enums.TipoPago;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FiltroPagoPeticion {
    
    private Long nitEntidad;
    
    private Integer anio;
    
    private Integer anioDesde;
    
    private Integer anioHasta;
    
    private LocalDate fechaDesde;
    
    private LocalDate fechaHasta;
    
    private Boolean verificado; // true para verificado, false para pendiente
    
    private TipoPago tipoPago;
    
    private Long idPensionado;
}

