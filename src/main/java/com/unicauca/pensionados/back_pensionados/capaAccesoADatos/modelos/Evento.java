package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.time.LocalDate;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.enums.TipoEvento;



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
}
