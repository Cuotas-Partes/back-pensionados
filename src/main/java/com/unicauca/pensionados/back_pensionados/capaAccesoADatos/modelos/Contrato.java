package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "CONTRATO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contrato {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idContrato")
    private Long idContrato;

    //Relacion 1:N con PENSIONADO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPersona", nullable = false)
    private Pensionado pensionado;

    //Relacion 1:N con ENTIDAD
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEntidad", nullable = false)
    private Entidad entidad;

    //Relacion 1:N con PERSONA
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "numeroIdentificacion", nullable = false)
    private Persona persona;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false) private
    LocalDate fechaFin;

    @Column(nullable = false, length = 120)
    private String cargo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoVinculacion tipoVinculacion;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal salarioBase;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal ultimoSalarioReportado;

    @Column(nullable = false)
    private Integer tiempoServicioDias;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private EstadoContrato estado;

    @Lob @Basic(fetch = FetchType.LAZY)
    @Column(name = "certificado_laboral", columnDefinition = "LONGBLOB")
    private byte[] certificadoLaboral;

    public enum TipoVinculacion { PLANTA, CATEDRATICO, OCASIONAL, OPS }
    public enum EstadoContrato { ACTIVO, FINALIZADO, SUSPENDIDO }
}

