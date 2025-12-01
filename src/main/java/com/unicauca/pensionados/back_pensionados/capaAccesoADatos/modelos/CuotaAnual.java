package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUOTA_ANUAL")
public class CuotaAnual {

    @Id
    @Column(name = "idCuotaAnual", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuotaAnual;

    @Column(name = "anio", nullable = false, unique = true)
    private Long anio;

    @Column(name = "valorIpc", nullable = false)
    private Double valorIpc;

    @Column(name = "usuarioRegistra", nullable = false, length = 100)
    private String usuarioRegistra;

    @Column(name = "salarioMinimoVigente", nullable = false)
    private Double salarioMinimoVigente;

    @Column(name = "fechaRegistro", nullable = false, length = 50)
    private String fechaRegistro = java.time.LocalDate.now().toString();

    @Column(name = "uvt", nullable = false)
    private Double UVT;

    @Column(name = "tasaInteresMoraAnual", nullable = false)
    private Double tasaInteresMoraAnual;

    @Column(name = "pIncrementoPensionalAnual", nullable = false)
    private Double pIncrementoPensionalAnual;
}
