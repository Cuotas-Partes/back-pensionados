package com.unicauca.pensionados.backend.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.backend.application.service.IReporteServicio;
import com.unicauca.pensionados.backend.application.dto.request.ReporteDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.ReporteDTORespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "API para gestionar reportes del sistema")
public class ReporteControlador {

    private final IReporteServicio reporteServicio;

    @PostMapping
    @Operation(summary = "Crear un nuevo reporte")
    public ResponseEntity<ReporteDTORespuesta> crear(@Valid @RequestBody ReporteDTOPeticion peticion) {
        ReporteDTORespuesta respuesta = reporteServicio.crear(peticion);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reporte por ID")
    public ResponseEntity<ReporteDTORespuesta> obtenerPorId(@PathVariable Long id) {
        ReporteDTORespuesta respuesta = reporteServicio.obtenerPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los reportes")
    public ResponseEntity<List<ReporteDTORespuesta>> obtenerTodos() {
        List<ReporteDTORespuesta> reportes = reporteServicio.obtenerTodos();
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Obtener reportes por tipo")
    public ResponseEntity<List<ReporteDTORespuesta>> obtenerPorTipo(@PathVariable String tipo) {
        List<ReporteDTORespuesta> reportes = reporteServicio.obtenerPorTipo(tipo);
        return ResponseEntity.ok(reportes);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Obtener reportes por usuario")
    public ResponseEntity<List<ReporteDTORespuesta>> obtenerPorUsuario(@PathVariable Integer usuarioId) {
        List<ReporteDTORespuesta> reportes = reporteServicio.obtenerPorUsuario(usuarioId);
        return ResponseEntity.ok(reportes);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un reporte")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        reporteServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
