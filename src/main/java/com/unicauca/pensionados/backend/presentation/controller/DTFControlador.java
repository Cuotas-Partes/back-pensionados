package com.unicauca.pensionados.backend.presentation.controller;

import com.unicauca.pensionados.backend.application.dto.request.dtf.DtfRequestDTO;
import com.unicauca.pensionados.backend.application.dto.response.dtf.DTFDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.IDTFServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dtf")
@CrossOrigin(origins = "*")
public class DTFControlador {

    @Autowired
    private IDTFServicio dtfServicio;


    @GetMapping
    public ResponseEntity<?> obtener(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String periodo
    ) {
        try {
            if (id != null) {
                return ResponseEntity.ok(dtfServicio.obtenerDTFPorId(id));
            }

            if (periodo != null) {
                return ResponseEntity.ok(dtfServicio.obtenerPorPeriodo(periodo));
            }

            List<DTFDTO> lista = dtfServicio.listarDTFs();
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            return error("Error al obtener DTF", e);
        }
    }


    @PostMapping
    public ResponseEntity<?> crear(@RequestBody DtfRequestDTO request) {
        try {
            validarPeriodo(request.getPeriodo());

            DTFDTO creado = dtfServicio.guardarDTF(request);
            return ResponseEntity.ok(creado);

        } catch (BusinessValidationException e) {
            return badRequest(e.getMessage());
        } catch (Exception e) {
            return error("Error al crear DTF", e);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody DtfRequestDTO request
    ) {
        try {
            validarPeriodo(request.getPeriodo());

            DTFDTO actualizado = dtfServicio.actualizarDTF(id, request);
            return ResponseEntity.ok(actualizado);

        } catch (BusinessValidationException e) {
            return badRequest(e.getMessage());
        } catch (Exception e) {
            return error("Error al actualizar DTF", e);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            dtfServicio.eliminarDTF(id);

            Map<String, String> response = new HashMap<>();
            response.put("estado", "exito");
            response.put("mensaje", "DTF eliminado correctamente");

            return ResponseEntity.ok(response);

        } catch (BusinessValidationException e) {
            return badRequest(e.getMessage());
        } catch (Exception e) {
            return error("Error al eliminar DTF", e);
        }
    }

    /* =========================
       VALIDACIONES
       ========================= */

    private void validarPeriodo(String periodo) {
        if (periodo == null || !periodo.matches("\\d{4}-\\d{2}")) {
            throw new BusinessValidationException(
                    "El periodo debe tener el formato YYYY-MM"
            );
        }
    }

    /* =========================
       RESPUESTAS COMUNES
       ========================= */

    private ResponseEntity<Map<String, String>> error(String mensaje, Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "error");
        response.put("mensaje", mensaje + ": " + e.getMessage());
        return ResponseEntity.status(500).body(response);
    }

    private ResponseEntity<Map<String, String>> badRequest(String mensaje) {
        Map<String, String> response = new HashMap<>();
        response.put("estado", "error");
        response.put("mensaje", mensaje);
        return ResponseEntity.badRequest().body(response);
    }
}
