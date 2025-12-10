package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.domain.model.entity.Evento;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Persona;
import com.unicauca.pensionados.backend.domain.model.entity.Sucesor;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EventoRepository;
import com.unicauca.pensionados.backend.domain.model.enums.TipoEvento;
import org.springframework.beans.factory.annotation.Autowired;
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

