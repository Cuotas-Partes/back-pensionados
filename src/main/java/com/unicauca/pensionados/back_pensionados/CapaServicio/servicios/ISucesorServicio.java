package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroSucesorPeticion;
import java.util.List;

public interface ISucesorServicio {
    void registrarSucesor(RegistroSucesorPeticion request);
    List<RegistroSucesorPeticion> listaSucesores();
    RegistroSucesorPeticion obtenerSucesorPorId(Long idPersona);
    void eliminarSucesor(Long idPersona);
    void editarSucesor(Long idPersona, RegistroSucesorPeticion request);
}
