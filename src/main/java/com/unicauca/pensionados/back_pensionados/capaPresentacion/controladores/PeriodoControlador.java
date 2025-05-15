package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IPeriodoServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PeriodoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.EditarPeriodoPeticion;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    @PutMapping("/editar")
    public ResponseEntity<?> editarPeriodo(@RequestBody EditarPeriodoPeticion peticion) {
        try {
            periodoServicio.editarPeriodo(peticion);
            return ResponseEntity.ok("Periodo actualizado exitosamente");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar periodo: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + ex.getMessage());
        }
    }
}
