package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Eventos;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Evento;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Persona;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Sucesor;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EventoRepository;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.enums.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EventoServicio {

    @Autowired
    private EventoRepository eventoRepository;

    public void registrarEvento(Long idPersona, Long idPensionado, TipoEvento tipoEvento,
                                String usuario, String tablaAfectada, String descripcion) {

        Evento evento = new Evento();
        Pensionado pensionado = new Pensionado();
        pensionado.setIdPersona(idPensionado); // ✅ Usa el método heredado de Persona
        evento.setPensionado(pensionado);
        Persona persona = new Sucesor(); // si Sucesor extiende Persona
        persona.setIdPersona(idPersona);
        evento.setPersona(persona);

        evento.setTipoEvento(tipoEvento);
        evento.setUsuario(usuario);
        evento.setTablaAfectada(tablaAfectada);
        evento.setDescripcion(descripcion);
        evento.setFechaEvento(LocalDate.now());

        eventoRepository.save(evento);
    }

    //@EventListener
    //public void manejarDefuncion(DefuncionEvent evento) {
        // lógica de procesar defunción
    //}
}

