package com.unicauca.pensionados.backend.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResolucionDTORespuesta {
    
    private Long id;
    private String numeroResolucion;
    private LocalDate fechaResolucion;
    private BigDecimal valorResolucion;
    private EstadoResolucion estado;
    private TipoResolucion tipoResolucion;
    private Long pensionadoId;
    private String pensionadoNombre;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ResolucionDTORespuesta(Long id, String numeroResolucion, LocalDate fechaResolucion, BigDecimal valorResolucion, EstadoResolucion estado, TipoResolucion tipoResolucion) {
    }
}
