package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.service.interfaces.IDeudaServicio;
import com.unicauca.pensionados.backend.domain.model.entity.Deuda;
import com.unicauca.pensionados.backend.application.dto.response.DeudaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/deuda")
@CrossOrigin(origins = "*")
public class DeudaControlador {

    @Autowired
    private IDeudaServicio deudaServicio;

    @GetMapping
    public ResponseEntity<?> listarDeudas(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) Deuda.TipoDeuda tipoDeuda,
            @RequestParam(required = false) Deuda.EstadoDeuda estadoDeuda,
            @RequestParam(required = false) Long idPersona
    ) {
        try{
            if(id != null)
                return ResponseEntity.ok(deudaServicio.obtenerDeudaPorId(id));
            if(tipoDeuda != null || estadoDeuda != null || idPersona != null)
                return ResponseEntity.ok(deudaServicio.obtenerDeudasPorTipoEstadoPersona(tipoDeuda, estadoDeuda, idPersona));
            return ResponseEntity.ok(deudaServicio.listarDeudas());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("estado", "error");
            response.put("mensaje", "Error al listar las deudas: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearDeuda(@RequestBody DeudaDTO deudaDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            deudaDTO = deudaServicio.crearDeuda(deudaDTO);
            return ResponseEntity.ok(deudaDTO);
        } catch (RuntimeException e){
            response.put("estado", "error");
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
        catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al crear la deuda: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarDeuda(@RequestBody DeudaDTO deudaDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            if(deudaDTO.getIdDeuda() == null) throw new RuntimeException("El id de la deuda es obligatorio para actualizar");
            deudaServicio.actualizarDeuda(deudaDTO);
            return ResponseEntity.ok(deudaDTO);
        }catch (RuntimeException e){
            response.put("estado", "error");
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
        catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al actualizar la deuda: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarDeuda(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            deudaServicio.eliminarDeuda(id);
            response.put("estado", "exito");
            response.put("mensaje", "Deuda eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al eliminar la deuda: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
