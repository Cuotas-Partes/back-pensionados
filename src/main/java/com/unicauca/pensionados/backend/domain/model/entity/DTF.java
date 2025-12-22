package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "dtf"
)
public class DTF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDtf", nullable = false, unique = true)
    private Long idDtf;

    @Column(name = "periodo", nullable = false)
    private String periodo; // Formato: "YYYY-MM"

    @Column(name="valor" , nullable = false, precision = 10, scale = 4)
    private BigDecimal valor;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "estado", nullable = false)
    private boolean estado = true;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
