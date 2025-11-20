package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Eventos;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORICO_LIQUIDACION_POR_COBRAR")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoLiquidacionPorCobrar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idLiquidacion")
    private Long idLiquidacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idPensionado", nullable = false)
    private Pensionado pensionado;    // FK -> Persona/Pensionado

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idEntidad", nullable = false)
    private Entidad entidad;          // FK -> ENTIDAD

    @Column(name = "fechaInicioPeriodo", nullable = false)
    private LocalDate fechaInicioPeriodo;

    @Column(name = "fechaFinPeriodo", nullable = false)
    private LocalDate fechaFinPeriodo;

    @Column(name = "numeroMesadas", precision = 5, scale = 2, nullable = false)
    private BigDecimal numeroMesadas;

    @Column(name = "valorPension", precision = 19, scale = 2, nullable = false)
    private BigDecimal valorPension;

    @Column(name = "valorCuotaParteMensual", precision = 19, scale = 2, nullable = false)
    private BigDecimal valorCuotaParteMensual;

    @Column(name = "cuotaParteTotalAnio", precision = 19, scale = 2, nullable = false)
    private BigDecimal cuotaParteTotalAnio;

    @Column(name = "porcentajeCuotaParte", precision = 5, scale = 2, nullable = false)
    private BigDecimal porcentajeCuotaParte;

    @Column(name = "porcentajeIncrementoAnual", precision = 5, scale = 2)
    private BigDecimal porcentajeIncrementoAnual;

    @Column(name = "incrementoAdicionalLey476", precision = 19, scale = 2)
    private BigDecimal incrementoAdicionalLey476;

    @Column(name = "valorInicialPension", precision = 19, scale = 2, nullable = false)
    private BigDecimal valorInicialPension;

    @Column(name = "fechaInicioPension")
    private LocalDate fechaInicioPension;

    @Column(name = "resolucionPension", length = 200)
    private String resolucionPension;

    @Column(name = "fechaFallecimientoPensionado")
    private LocalDate fechaFallecimientoPensionado;

    @Column(name = "nombreSustituto", length = 150)
    private String nombreSustituto;

    @Column(name = "documentoSustituto", length = 20)
    private String documentoSustituto;

    @Column(name = "notas", length = 500)
    private String notas;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoLiquidacion", length = 15, nullable = false)
    private EstadoLiquidacion estadoLiquidacion;

    @Column(name = "fechaRegistro", nullable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime fechaRegistro;
    @Column(name = "usuarioRegistro", length = 50, nullable = false)
    private String usuarioRegistro;

    public enum EstadoLiquidacion {
        PENDIENTE,
        COBRADA,
        ANULADA,
        EN_TRAMITE
    }
}
