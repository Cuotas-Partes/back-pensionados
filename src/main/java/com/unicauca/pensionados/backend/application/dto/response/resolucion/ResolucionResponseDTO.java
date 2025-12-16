package com.unicauca.pensionados.backend.application.dto.response.resolucion;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResolucionResponseDTO {

    private Long id;
    private String numeroResolucion;
    private LocalDate fechaResolucion;
    private BigDecimal valorResolucion;
    private EstadoResolucion estado;
    private TipoResolucion tipoResolucion;
    private String observaciones;
    private Map<String, Object> datosEspecificos;
}
