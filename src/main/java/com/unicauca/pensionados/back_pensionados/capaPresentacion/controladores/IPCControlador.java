package com.unicauca.pensionados.back_pensionados.capaPresentacion.controladores;

import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.IIPCServicio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroIPCPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.IPCRespuestaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST para la gestión de los valores del IPC.
 * Proporciona endpoints para listar, consultar, registrar, actualizar y eliminar IPC.
 */
@RestController
@RequestMapping("/ipc")
public class IPCControlador {
    @Autowired
    private IIPCServicio ipcServicio;

    /**
     * Lista todos los registros de IPC.
     * @return Lista de DTOs con los IPC registrados.
     */
    @GetMapping("/listar")
    public List<IPCRespuestaDTO> listarIPC() {
        return ipcServicio.listarIPC();
    }

    /**
     * Consulta el IPC de un año específico.
     * @param anio Año a consultar.
     * @return DTO con el valor del IPC, o 404 si no existe.
     */
    @GetMapping("buscarPorAnio/{anio}")
    public ResponseEntity<IPCRespuestaDTO> buscarIPCPorAnio(@PathVariable Integer anio) {
        IPCRespuestaDTO ipc = ipcServicio.buscarIPCPorAnio(anio);
        if (ipc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ipc);
    }

    /**
     * Registra un nuevo valor de IPC.
     * @param peticion DTO con los datos del IPC a registrar.
     * @return Mensaje de éxito o error.
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarIPC(@RequestBody RegistroIPCPeticion peticion) {
        try {
            ipcServicio.registrarIPC(peticion);
            return ResponseEntity.ok("IPC registrado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar IPC: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Actualiza el valor del IPC para el año dado.
     * @param anio Año del IPC a actualizar.
     * @param peticion DTO con el nuevo valor del IPC.
     * @return Mensaje de éxito o error.
     */
    @PutMapping("/actualizar/{anio}")
    public ResponseEntity<?> actualizarIPC(@PathVariable Integer anio, @RequestBody RegistroIPCPeticion peticion) {
        try {
            ipcServicio.actualizarIPC(anio, peticion);
            return ResponseEntity.ok("IPC actualizado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar IPC: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Elimina el registro de IPC para el año dado.
     * @param anio Año del IPC a eliminar.
     * @return Mensaje de éxito o error.
     */
    @DeleteMapping("/eliminar/{anio}")
    public ResponseEntity<?> eliminarIPC(@PathVariable Integer anio) {
        try {
            ipcServicio.eliminarIPC(anio);
            return ResponseEntity.ok("IPC eliminado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al eliminar IPC: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + e.getMessage());
        }
    }
}
