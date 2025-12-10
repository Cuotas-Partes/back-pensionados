package com.unicauca.pensionados.backend.domain.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.unicauca.pensionados.backend.domain.model.enums.TipoEvento;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "EVENTO")
@Getter
@Setter
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEvento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPersona")
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPensionado")
    private Pensionado pensionado;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoEvento", nullable = false, length = 50)
    private TipoEvento tipoEvento;

    @Column(name = "fechaEvento", nullable = false)
    private LocalDate fechaEvento;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "tablaAfectada", nullable = false, length = 100)
    private String tablaAfectada;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    // Campos adicionales para novedades detalladas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pensionadoSustitutoId")
    private Pensionado pensionadoSustituto;

    @Column(name = "resolucionAnterior", columnDefinition = "TEXT")
    private String resolucionAnterior; // JSON

    @Column(name = "resolucionNueva", columnDefinition = "TEXT")
    private String resolucionNueva; // JSON

    @Column(name = "cambiosRealizados", columnDefinition = "TEXT")
    private String cambiosRealizados; // JSON con los campos que cambiaron

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioId")
    private Usuario usuarioRegistro;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
