package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.ICuotaAnualServicio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaAnual;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaAnualDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuota-anual")
public class CuotaAnualControlador {

    @Autowired
    private ICuotaAnualServicio cuotaAnualServicio;

    @GetMapping
    public ResponseEntity<?> listarCuotasAnuales(
            @RequestParam(required = false) Long anio,
            @RequestParam(required = false) Long id
    ) {
        try {
            if(anio != null) return ResponseEntity.ok(cuotaAnualServicio.obtenerCuotaAnualPorAnio(anio));
            if(id != null) return ResponseEntity.ok(cuotaAnualServicio.obtenerCuotaAnualPorId(id));
            List<CuotaAnualDTO> cuotasAnuales = cuotaAnualServicio.listarCuotasAnuales();
            return ResponseEntity.ok(cuotasAnuales);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("estado", "error");
            response.put("mensaje", "Error al listar las cuotas anuales: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> crearCuotaAnual(@RequestBody CuotaAnualDTO cuotaAnualDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            if (cuotaAnualDTO.getAnio() == null) throw new RuntimeException("El año es obligatorio");
            if (cuotaAnualDTO.getValorIpc() == null) throw new RuntimeException("El valor del IPC es obligatorio");
            if (cuotaAnualDTO.getUsuarioRegistra() == null) throw new RuntimeException("El usuario que registra es obligatorio");
            if (cuotaAnualDTO.getSalarioMinimoVigente() == null) throw new RuntimeException("El salario mínimo vigente es obligatorio");
            if (cuotaAnualDTO.getFechaRegistro() == null) throw new RuntimeException("La fecha de registro es obligatoria");
            if (cuotaAnualDTO.getUVT() == null) throw new RuntimeException("La UVT es obligatoria");
            if (cuotaAnualDTO.getTasaInteresMoraAnual() == null) throw new RuntimeException("La tasa de interés de mora anual es obligatoria");
            if (cuotaAnualDTO.getPIncrementoPensionalAnual() == null) throw new RuntimeException("El porcentaje de incremento pensional anual es obligatorio");

            cuotaAnualDTO = cuotaAnualServicio.guardarCuotaAnual(cuotaAnualDTO);
            return ResponseEntity.ok(cuotaAnualDTO);
        } catch (RuntimeException e) {
            response.put("estado", "error");
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al crear la cuota anual: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> actualizarCuotaAnual(@RequestBody CuotaAnualDTO cuotaAnualDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            if (cuotaAnualDTO.getIdCuotaAnual() == null) throw new RuntimeException("El id de la cuota anual es obligatorio para actualizar");
            cuotaAnualServicio.actualizarCuotaAnual(cuotaAnualDTO);
            return ResponseEntity.ok(cuotaAnualDTO);
        }catch (RuntimeException e){
            response.put("estado", "error");
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
        catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al actualizar la cuota anual: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> eliminarCuotaAnual(@RequestParam Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            cuotaAnualServicio.eliminarCuotaAnual(id);
            response.put("estado", "exito");
            response.put("mensaje", "Cuota anual eliminada correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("estado", "error");
            response.put("mensaje", "Error al eliminar la cuota anual: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }



}
