package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SucesorRespuesta {

    // Campos de Persona
    private Long numeroIdPersona;
    private String tipoIdPersona;
    private String nombrePersona;
    private String apellidosPersona;
    private Date fechaNacimientoPersona;
    private Date fechaExpedicionDocumentoIdPersona;
    private String estadoPersona;
    private String generoPersona;
    private Date fechaDefuncionPersona;

    
    private Date fechaInicioSucesion;
    private Double porcentajePension;
} 