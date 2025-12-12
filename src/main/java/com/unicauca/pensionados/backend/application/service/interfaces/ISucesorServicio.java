package com.unicauca.pensionados.backend.application.service.interfaces;

import com.unicauca.pensionados.backend.application.dto.request.RegistroSucesorPeticion;
import java.util.List;

public interface ISucesorServicio {
    void registrarSucesor(RegistroSucesorPeticion request);
    List<RegistroSucesorPeticion> listaSucesores();
    RegistroSucesorPeticion obtenerSucesorPorId(Long idPersona);
    void eliminarSucesor(Long idPersona);
    void editarSucesor(Long idPersona, RegistroSucesorPeticion request);
}
