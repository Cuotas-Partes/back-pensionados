package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.dto.request.cuota.RegistroCuotaPagarPeticion;
import com.unicauca.pensionados.backend.application.dto.response.cuota.CuotaPagarRespuestaDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.ICuotaPagarServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuota-pagar")
@CrossOrigin(origins = "*")
public class CuotaPagarControlador {

    private final ICuotaPagarServicio cuotaPagarServicio;

    public CuotaPagarControlador(ICuotaPagarServicio cuotaPagarServicio) {
        this.cuotaPagarServicio = cuotaPagarServicio;
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CuotaPagarRespuestaDTO>> listar() {
        return ResponseEntity.ok(cuotaPagarServicio.listar());
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cuotaPagarServicio.obtenerPorId(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody RegistroCuotaPagarPeticion peticion) {
        try {
            CuotaPagarRespuestaDTO creado = cuotaPagarServicio.crear(peticion);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody RegistroCuotaPagarPeticion peticion) {
        try {
            return ResponseEntity.ok(cuotaPagarServicio.actualizar(id, peticion));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            cuotaPagarServicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
