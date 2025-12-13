package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoResolucion;
import com.unicauca.pensionados.backend.domain.model.enums.TipoResolucion;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resolucion")
@Getter
@Setter
public class Resolucion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numeroResolucion", nullable = false, unique = true, length = 100)
    private String numeroResolucion;

    @Column(name = "fechaResolucion", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDate fechaResolucion;

    @Column(name = "valorResolucion", nullable = false, precision = 19, scale = 2)
    private BigDecimal valorResolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoResolucion estado = EstadoResolucion.VIGENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoResolucion", nullable = false, length = 50)
    private TipoResolucion tipoResolucion;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPensionado", nullable = false)
    private Pensionado pensionado;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
