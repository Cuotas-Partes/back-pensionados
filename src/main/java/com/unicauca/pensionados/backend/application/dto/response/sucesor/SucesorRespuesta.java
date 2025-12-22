package com.unicauca.pensionados.backend.application.dto.response.sucesor;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoSustituto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.unicauca.pensionados.backend.domain.model.enums.EstadoPersona;
import com.unicauca.pensionados.backend.domain.model.enums.Genero;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los datos de un sucesor; incluye información personal y de sucesión.")
public class SucesorRespuesta {

    Long numeroDocumento;
    TipoIdentificacion tipoIdentificacion;
    String nombreCompleto;
    EstadoSustituto estado;
    LocalDate fechaInicio;
    LocalDate fechaFin;
    BigDecimal porcentajePension;
    Long resolucionNombramientoId; // Solo el ID; no todo el objeto
    String numeroResolucionNombramiento; // Para mostrar fácilmente
}
