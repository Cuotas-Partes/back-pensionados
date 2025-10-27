package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name = "ENTIDAD")
@Getter @Setter
public class Entidad {
    @Id //llave primaria
    @Column (name = "nitEntidad" , nullable = false, unique = true)
    //NIT de la entidad, no puede ser nulo y debe ser unico
    private Long nitEntidad;

    @Column (name = "nombreEntidad", nullable = false, length = 100, unique = true)
    //nombre de la entidad, no puede ser nulo y debe ser unico
    private String nombreEntidad;

    @Column (name ="direccionEntidad", nullable = false, length =100)
    //direccion de la entidad, no puede ser nulo
    private String direccionEntidad;

    @Column (name = "emailEntidad", nullable =  false, length = 100)
    //email de la entidad, no puede ser nulo
    private String emailEntidad;

    @Column (name = "telefonoEntidad", nullable = false)
    //telefono de la entidad, no puede ser nulo
    private Long telefonoEntidad;    

    @Column (name = "estadoEntidad", nullable = false, length = 50)
    //estado de la entidad, no puede ser nulo
    private String estadoEntidad;

    //relacion 1 a muchos Pensonados
    @JsonManagedReference //rompe el ciclo infinito de serializacion al mostrar el JSON
    @OneToMany(mappedBy = "entidadJubilacion")
    private List<Pensionado> pensionados;

    //relacion 1 a muchos pensionados que trabajaron en la entidad
    @JsonManagedReference(value = "entidad-trabajo")
    @OneToMany(mappedBy = "entidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trabajo> trabajos = new ArrayList<>();
}

