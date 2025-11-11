package com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Eventos;
import java.time.LocalDate;

public class DefuncionEvent {
    private Long pensionadoId;
    private LocalDate fechaDefuncion;
    private String usuario;

    public DefuncionEvent(Long pensionadoId, LocalDate fechaDefuncion, String usuario) {
        this.pensionadoId = pensionadoId;
        this.fechaDefuncion = fechaDefuncion;
        this.usuario = usuario;
    }

    public Long getPensionadoId() {
        return pensionadoId;
    }

    public LocalDate getFechaDefuncion() {
        return fechaDefuncion;
    }

    public String getUsuario() {
        return usuario;
    }
}

