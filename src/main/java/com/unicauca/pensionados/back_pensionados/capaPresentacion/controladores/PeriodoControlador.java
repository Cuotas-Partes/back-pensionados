package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPeriodoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/periodo")
public class PeriodoControlador {

    @Autowired
    private IPeriodoServicio periodoServicio;

    @GetMapping("/buscarPorAnio/{anio}")
    public ResponseEntity<?> consultarPeriodoPorAnio(@PathVariable int anio) {
        try {
            PeriodoRespuesta respuesta = periodoServicio.consultarPeriodoPorAnio(anio);
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }
}
