package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "ipc")
@Getter @Setter
public class IPC {
    @Id
    @Column (name = "fechaIPC", nullable = false)
    private Integer fechaIPC;
    
    @Column (name = "valorIPC", nullable = false, precision = 5, scale = 2)
    private BigDecimal valorIPC;
}
