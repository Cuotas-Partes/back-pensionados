package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IDTFServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DTFDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dtf")
public class DTFControlador {

    @Autowired
    private IDTFServicio dtfServicio;

    @GetMapping
    public ResponseEntity<?> getDTF(
            @RequestParam(required = false) Long mes,
            @RequestParam(required = false) Long anio,
            @RequestParam(required = false) Long id
    ) {
        try {
            if(id != null) return ResponseEntity.ok(dtfServicio.obtenerDTFPorId(id));
            // Se requiere que lleguen ambos para mayor precision
            // Se obtienen los de los meses de cualquier año y del año de cualquier mes
            if(mes != null || anio != null) return ResponseEntity.ok(dtfServicio.obtenerDTFPorMesAnio(mes, anio));
            return ResponseEntity.ok(dtfServicio.listarDTFs());
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("estado", "error");
            response.put("mensaje", "Error al obtener los DTFs: " + e.getMessage());

            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearDTF(@RequestBody DTFDTO dtfDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            if (dtfDTO.getMes() == null || dtfDTO.getAnio() == null) throw new RuntimeException("El mes y el año son obligatorios");
            if(dtfDTO.getMes() < 1 || dtfDTO.getMes() > 12) throw new RuntimeException("El mes debe estar entre 1 y 12");
            if(dtfDTO.getAnio() < 1900 || dtfDTO.getAnio() > LocalDate.now().getYear()) throw new RuntimeException("El año debe estar entre 1900 y el año actual");

            dtfDTO = dtfServicio.guardarDTF(dtfDTO);
            return ResponseEntity.ok(dtfDTO);
        } catch (RuntimeException e) {
            response.put("estado", "error");
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
        catch (Exception e){
            response.put("estado", "error");
            response.put("mensaje", "Error al crear el DTF: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarDTF(@RequestBody com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DTFDTO dtfDTO) {
        try {
            if (dtfDTO.getMes() == null || dtfDTO.getAnio() == null) throw new RuntimeException("El mes y el año son obligatorios");
            if(dtfDTO.getMes() < 1 || dtfDTO.getMes() > 12) throw new RuntimeException("El mes debe estar entre 1 y 12");
            if(dtfDTO.getAnio() < 1900 || dtfDTO.getAnio() > LocalDate.now().getYear()) throw new RuntimeException("El año debe estar entre 1900 y el año actual");

            dtfServicio.actualizarDTF(dtfDTO);
            return ResponseEntity.ok(dtfDTO);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("estado", "error");
            response.put("mensaje", "Error al actualizar el DTF: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarDTF(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            dtfServicio.eliminarDTF(id);
            response.put("estado", "exito");
            response.put("mensaje", "DTF eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al eliminar el DTF: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
