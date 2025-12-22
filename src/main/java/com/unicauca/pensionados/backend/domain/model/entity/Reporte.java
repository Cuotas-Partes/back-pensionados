package com.unicauca.pensionados.backend.domain.model.entity;

import java.time.LocalDateTime;

import com.unicauca.pensionados.backend.domain.model.enums.TipoReporte;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reporte")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 50)
    private TipoReporte tipo;

    @Column(name = "filtros", columnDefinition = "TEXT")
    private String filtros; // JSON con filtros aplicados

    @Column(name = "datos", columnDefinition = "LONGTEXT")
    private String datos; // JSON con los datos del reporte

    @Column(name = "totalRegistros")
    private Integer totalRegistros;

    @Column(name = "totalValor", precision = 19, scale = 2)
    private java.math.BigDecimal totalValor;

    @Column(name = "totalCorriente", precision = 19, scale = 2)
    private java.math.BigDecimal totalCorriente;

    @Column(name = "totalNoCorriente", precision = 19, scale = 2)
    private java.math.BigDecimal totalNoCorriente;

    @Column(name = "totalPrescrito", precision = 19, scale = 2)
    private java.math.BigDecimal totalPrescrito;

    @Column(name = "generadoEn", nullable = false)
    private LocalDateTime generadoEn = LocalDateTime.now();

    @Column(name = "generadoPor", length = 100)
    private String generadoPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;
}
