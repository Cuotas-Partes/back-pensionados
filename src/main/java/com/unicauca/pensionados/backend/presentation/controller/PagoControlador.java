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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unicauca.pensionados.backend.application.service.interfaces.IPagoServicio;
import com.unicauca.pensionados.backend.domain.model.enums.TipoPago;
import com.unicauca.pensionados.backend.application.dto.request.FiltroPagoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.PagoDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.PagoDTORespuesta;
import com.unicauca.pensionados.backend.application.dto.response.ResumenPagoDTORespuesta;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/pagos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "API para gestionar el informe financiero de pagos")
public class PagoControlador {

    private final IPagoServicio pagoServicio;

    @PostMapping
    @Operation(summary = "Registrar un nuevo pago", 
               description = "Registra un nuevo pago con validaciones de entidad, pensionado y fecha no duplicada. Calcula automáticamente saldo, indexación y saldo pendiente.")
    public ResponseEntity<PagoDTORespuesta> crear(@Valid @RequestBody PagoDTOPeticion peticion) {
        PagoDTORespuesta respuesta = pagoServicio.crear(peticion);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pago existente", 
               description = "Actualiza un pago existente y recalcula automáticamente saldo, indexación y saldo pendiente.")
    public ResponseEntity<PagoDTORespuesta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoDTOPeticion peticion) {
        PagoDTORespuesta respuesta = pagoServicio.actualizar(id, peticion);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pago", 
               description = "Elimina un pago del sistema.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoServicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pago por ID", 
               description = "Obtiene los detalles completos de un pago incluyendo todos los cálculos.")
    public ResponseEntity<PagoDTORespuesta> obtenerPorId(@PathVariable Long id) {
        PagoDTORespuesta respuesta = pagoServicio.obtenerPorId(id);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @Operation(summary = "Listar pagos con filtros", 
               description = "Obtiene una lista de pagos aplicando filtros opcionales: entidad, año, rango de fechas, estado de verificación y tipo de pago.")
    public ResponseEntity<List<PagoDTORespuesta>> listarConFiltros(
            @RequestParam(required = false) Long entidad,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer anioDesde,
            @RequestParam(required = false) Integer anioHasta,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) Boolean verificado,
            @RequestParam(required = false) TipoPago tipoPago,
            @RequestParam(required = false) Long idPensionado) {
        
        FiltroPagoPeticion filtro = new FiltroPagoPeticion();
        filtro.setNitEntidad(entidad);
        filtro.setAnio(anio);
        filtro.setAnioDesde(anioDesde);
        filtro.setAnioHasta(anioHasta);
        
        if (fechaDesde != null && !fechaDesde.isEmpty()) {
            filtro.setFechaDesde(java.time.LocalDate.parse(fechaDesde));
        }
        if (fechaHasta != null && !fechaHasta.isEmpty()) {
            filtro.setFechaHasta(java.time.LocalDate.parse(fechaHasta));
        }
        
        filtro.setVerificado(verificado);
        filtro.setTipoPago(tipoPago);
        filtro.setIdPensionado(idPensionado);
        
        List<PagoDTORespuesta> pagos = pagoServicio.buscarConFiltros(filtro);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/entidad/{nitEntidad}")
    @Operation(summary = "Obtener pagos por entidad", 
               description = "Obtiene todos los pagos realizados por una entidad específica.")
    public ResponseEntity<List<PagoDTORespuesta>> obtenerPorEntidad(@PathVariable Long nitEntidad) {
        List<PagoDTORespuesta> pagos = pagoServicio.obtenerPorEntidad(nitEntidad);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/anio/{anio}")
    @Operation(summary = "Obtener pagos por año", 
               description = "Obtiene todos los pagos realizados en un año específico.")
    public ResponseEntity<List<PagoDTORespuesta>> obtenerPorAnio(@PathVariable Integer anio) {
        List<PagoDTORespuesta> pagos = pagoServicio.obtenerPorAnio(anio);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/entidad/{nitEntidad}/anio/{anio}")
    @Operation(summary = "Obtener pagos por entidad y año", 
               description = "Obtiene todos los pagos realizados por una entidad en un año específico.")
    public ResponseEntity<List<PagoDTORespuesta>> obtenerPorEntidadYAnio(
            @PathVariable Long nitEntidad,
            @PathVariable Integer anio) {
        List<PagoDTORespuesta> pagos = pagoServicio.obtenerPorEntidadYAnio(nitEntidad, anio);
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/resumen")
    @Operation(summary = "Obtener resumen agrupado de pagos", 
               description = "Obtiene un resumen de pagos agrupados por año y entidad, con totales calculados. Permite aplicar los mismos filtros que el listado.")
    public ResponseEntity<List<ResumenPagoDTORespuesta>> obtenerResumen(
            @RequestParam(required = false) Long entidad,
            @RequestParam(required = false) Integer anio,
            @RequestParam(required = false) Integer anioDesde,
            @RequestParam(required = false) Integer anioHasta,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta,
            @RequestParam(required = false) Boolean verificado,
            @RequestParam(required = false) TipoPago tipoPago,
            @RequestParam(required = false) Long idPensionado) {
        
        FiltroPagoPeticion filtro = new FiltroPagoPeticion();
        filtro.setNitEntidad(entidad);
        filtro.setAnio(anio);
        filtro.setAnioDesde(anioDesde);
        filtro.setAnioHasta(anioHasta);
        
        if (fechaDesde != null && !fechaDesde.isEmpty()) {
            filtro.setFechaDesde(java.time.LocalDate.parse(fechaDesde));
        }
        if (fechaHasta != null && !fechaHasta.isEmpty()) {
            filtro.setFechaHasta(java.time.LocalDate.parse(fechaHasta));
        }
        
        filtro.setVerificado(verificado);
        filtro.setTipoPago(tipoPago);
        filtro.setIdPensionado(idPensionado);
        
        List<ResumenPagoDTORespuesta> resumen = pagoServicio.obtenerResumenAgrupado(filtro);
        return ResponseEntity.ok(resumen);
    }
}

