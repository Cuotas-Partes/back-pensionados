package com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Deuda;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Persona;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
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
