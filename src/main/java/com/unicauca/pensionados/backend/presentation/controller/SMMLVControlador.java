package com.unicauca.pensionados.backend.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.backend.application.service.ISMMLVServicio;
import com.unicauca.pensionados.backend.application.dto.request.SMMLVDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.SMMLVDTORespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/smmlv")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "SMMLV", description = "API para gestionar el histórico de SMMLV")
public class SMMLVControlador {

    private final ISMMLVServicio smmlvServicio;

    @PostMapping
    @Operation(summary = "Crear un nuevo registro de SMMLV")
    public ResponseEntity<SMMLVDTORespuesta> crear(@Valid @RequestBody SMMLVDTOPeticion peticion) {
        SMMLVDTORespuesta respuesta = smmlvServicio.crear(peticion);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro de SMMLV")
    public ResponseEntity<SMMLVDTORespuesta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SMMLVDTOPeticion peticion) {
        SMMLVDTORespuesta respuesta = smmlvServicio.actualizar(id, peticion);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de SMMLV")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        smmlvServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener SMMLV por ID")
    public ResponseEntity<SMMLVDTORespuesta> obtenerPorId(@PathVariable Long id) {
        SMMLVDTORespuesta respuesta = smmlvServicio.obtenerPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/ano/{ano}")
    @Operation(summary = "Obtener SMMLV por año")
    public ResponseEntity<SMMLVDTORespuesta> obtenerPorAno(@PathVariable Integer ano) {
        SMMLVDTORespuesta respuesta = smmlvServicio.obtenerPorAno(ano);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los registros de SMMLV")
    public ResponseEntity<List<SMMLVDTORespuesta>> obtenerTodos() {
        List<SMMLVDTORespuesta> smmlvList = smmlvServicio.obtenerTodos();
        return ResponseEntity.ok(smmlvList);
    }
}
