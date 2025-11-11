package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "DTF",
        uniqueConstraints = @UniqueConstraint(columnNames = {"mes", "anio"})
)
public class DTF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDtf", nullable = false, unique = true)
    private Long idDtf;

    @Column(name = "mes", nullable = false)
    private Long mes;

    @Column(name = "anio", nullable = false)
    private Long anio;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "usuario", nullable = false)
    private String usuario;

    @Column(name = "fechaRegistro", nullable = false)
    private String fechaRegistro = java.time.LocalDate.now().toString();

}
