package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "PERIODO")
@Getter @Setter
public class Periodo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPeriodo;

    @ManyToOne
    @JoinColumn(name = "fechaIPC", nullable = false)
    private IPC IPC;

    @Column (name = "fechaInicioPeriodo", nullable = false)
    private LocalDate fechaInicioPeriodo;

    @Column (name = "fechaFinPeriodo", nullable = false)
    private LocalDate fechaFinPeriodo;

    @Column (name = "numeroMesadas", nullable = false)
    private Long numeroMesadas;

    @OneToMany(mappedBy = "periodo", cascade = CascadeType.ALL)
    private List<CuotaParte> cuotasPartes;

}
