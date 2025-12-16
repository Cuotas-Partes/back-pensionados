package com.unicauca.pensionados.backend.application.dto.request.resolucion;

import com.unicauca.pensionados.backend.domain.model.enums.TipoPension;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class PensionInicialRequestDTO extends  ResolucionBaseRequestDTO{
    private LocalDate fechaInicio;
    @NotNull
    private TipoPension tipoPension;
}
