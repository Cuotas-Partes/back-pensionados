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

import com.unicauca.pensionados.backend.application.service.ILiquidacionPensionadoServicio;
import com.unicauca.pensionados.backend.application.dto.request.LiquidacionPensionadoDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.LiquidacionPensionadoDTORespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/liquidaciones-pensionado")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Liquidaciones Pensionado", description = "API para gestionar liquidaciones de pensionados")
public class LiquidacionPensionadoControlador {

    private final ILiquidacionPensionadoServicio liquidacionServicio;

    @PostMapping
    @Operation(summary = "Crear una nueva liquidación de pensionado")
    public ResponseEntity<LiquidacionPensionadoDTORespuesta> crear(@Valid @RequestBody LiquidacionPensionadoDTOPeticion peticion) {
        LiquidacionPensionadoDTORespuesta respuesta = liquidacionServicio.crear(peticion);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una liquidación de pensionado")
    public ResponseEntity<LiquidacionPensionadoDTORespuesta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LiquidacionPensionadoDTOPeticion peticion) {
        LiquidacionPensionadoDTORespuesta respuesta = liquidacionServicio.actualizar(id, peticion);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una liquidación")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        liquidacionServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener liquidación por ID")
    public ResponseEntity<LiquidacionPensionadoDTORespuesta> obtenerPorId(@PathVariable Long id) {
        LiquidacionPensionadoDTORespuesta respuesta = liquidacionServicio.obtenerPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las liquidaciones")
    public ResponseEntity<List<LiquidacionPensionadoDTORespuesta>> obtenerTodas() {
        List<LiquidacionPensionadoDTORespuesta> liquidaciones = liquidacionServicio.obtenerTodas();
        return ResponseEntity.ok(liquidaciones);
    }

    @GetMapping("/pensionado/{pensionadoId}")
    @Operation(summary = "Obtener liquidaciones por pensionado")
    public ResponseEntity<List<LiquidacionPensionadoDTORespuesta>> obtenerPorPensionado(@PathVariable Long pensionadoId) {
        List<LiquidacionPensionadoDTORespuesta> liquidaciones = liquidacionServicio.obtenerPorPensionado(pensionadoId);
        return ResponseEntity.ok(liquidaciones);
    }

    @GetMapping("/periodo/{periodoId}")
    @Operation(summary = "Obtener liquidaciones por periodo")
    public ResponseEntity<List<LiquidacionPensionadoDTORespuesta>> obtenerPorPeriodo(@PathVariable Long periodoId) {
        List<LiquidacionPensionadoDTORespuesta> liquidaciones = liquidacionServicio.obtenerPorPeriodo(periodoId);
        return ResponseEntity.ok(liquidaciones);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener liquidaciones por estado")
    public ResponseEntity<List<LiquidacionPensionadoDTORespuesta>> obtenerPorEstado(@PathVariable String estado) {
        List<LiquidacionPensionadoDTORespuesta> liquidaciones = liquidacionServicio.obtenerPorEstado(estado);
        return ResponseEntity.ok(liquidaciones);
    }
}
