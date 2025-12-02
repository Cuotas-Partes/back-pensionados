package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.util.ArrayList;
import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Eventos.HistoricoLiquidacionPorCobrar;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoEntidad;

@Entity
@Table(name = "ENTIDAD")
@Getter
@Setter
public class Entidad {
    // Nueva llame primaria auto incrementable
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEntidad")
    private Long idEntidad;

    // Nit unico (ya no es PK)
    @Column(name = "nitEntidad", nullable = false, unique = true)
    private Long nitEntidad;

    @Column(name = "nombreEntidad", nullable = false, length = 100, unique = true)
    // nombre de la entidad, no puede ser nulo y debe ser unico
    private String nombreEntidad;

    @Column(name = "direccionEntidad", nullable = false, length = 100)
    // direccion de la entidad, no puede ser nulo
    private String direccionEntidad;

    @Column(name = "emailEntidad", nullable = false, length = 100)
    // email de la entidad, no puede ser nulo
    private String emailEntidad;

    @Column(name = "telefonoEntidad", nullable = false)
    // telefono de la entidad, no puede ser nulo
    private Long telefonoEntidad;

    @Column(name = "estadoEntidad", nullable = false, length = 50)
    @Enumerated(EnumType.STRING) // guarda el nombre del enum como texto
    // estado de la entidad, no puede ser nulo
    private EstadoEntidad estadoEntidad;

    @Column(name = "esPagadora") // Campo para distinguir si paga o cobra cuotas partes
    private boolean esPagadora;    

    // relacion 1 a muchos Pensonados
    @JsonManagedReference // rompe el ciclo infinito de serializacion al mostrar el JSON
    @OneToMany(mappedBy = "entidadJubilacion")
    private List<Pensionado> pensionados;

    // relacion 1 a muchos pensionados que trabajaron en la entidad
    @JsonManagedReference(value = "entidad-trabajo")
    @OneToMany(mappedBy = "entidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trabajo> trabajos = new ArrayList<>();

    //Relación con HistoricoLiquidacionPorCobrar
    @OneToMany(mappedBy = "entidad", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<HistoricoLiquidacionPorCobrar> historicos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPersonaEncargado")
    private Persona encargado;

}
