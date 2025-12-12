package com.unicauca.pensionados.backend.domain.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "deuda")
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDeuda", nullable = false, unique = true)
    private Long idDeuda;

    @Column(name = "tipoDeuda", nullable = false)
    private TipoDeuda tipoDeuda;

    @Column(name = "estadoDeuda", nullable = false)
    private EstadoDeuda estadoDeuda;

    @OneToOne
    @JoinColumn(name = "idPersona", referencedColumnName = "idPersona")
    private Persona persona;

//    @OneToOne
//    @JoinColumn(name = "idPensionado", referencedColumnName = "idPensionado")
//    private Pensionado pensionado;

    @Column(name = "montoDeuda", nullable = false)
    private Double montoDeuda;

    @Column(name = "fechaCreacion", nullable = false)
    private String fechaCreacion = java.time.LocalDate.now().toString();

    @Column(name = "fechaVencimiento", nullable = false)
    private String fechaVencimiento;

    @Column(name = "tasaInteresAplicada", nullable = false)
    private Double tasaInteresAplicada;

    @Column(name = "usuarioRegistro", nullable = false)
    private String usuarioRegistro;

    public enum TipoDeuda {
        ACTIVO,
        PASIVO
    }
    public enum EstadoDeuda {
        VIGENTE,
        PRESCRITA,
        LITIGIO,
        PAGA,
        POR_PAGAR
    }

}



