package com.unicauca.pensionados.backend.domain.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicauca.pensionados.backend.application.dto.util.JsonConverter;
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

    @Column(name = "valorResolucion", precision = 19, scale = 2)
    private BigDecimal valorResolucion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoResolucion estado = EstadoResolucion.VIGENTE;

    @Column(name = "Observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoResolucion", nullable = false, length  = 50)
    private TipoResolucion tipoResolucion;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPensionado", nullable = false)

    //Campo JSON para datos específicos del tipo
    @Column(name = "datos_especificos", columnDefinition = "JSON")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> datosEspecificos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pensionado_id", nullable = false)
    @JsonBackReference
    private Pensionado pensionado;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (datosEspecificos == null) {
            datosEspecificos = new HashMap<>();
        }
    }
    // Métodos helper para acceder a datos específicos
    public Object getDatoEspecifico(String key) {
        return datosEspecificos != null ? datosEspecificos.get(key) : null;
    }

    public void setDatoEspecifico(String key, Object value) {
        if (datosEspecificos == null) {
            datosEspecificos = new HashMap<>();
        }
        datosEspecificos.put(key, value);
    }
}
