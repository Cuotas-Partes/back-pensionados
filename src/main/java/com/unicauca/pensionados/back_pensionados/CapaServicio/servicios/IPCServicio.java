package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.IPCRespuestaDTO;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroIPCPeticion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Year;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de los valores del IPC.
 */
@Service
public class IPCServicio implements IIPCServicio {
    @Autowired
    private IPCRepositorio ipcRepositorio;

    /**
     * Lista todos los registros de IPC existentes.
     * @return Lista de DTOs con los valores de IPC registrados.
     */
    @Override
    public List<IPCRespuestaDTO> listarIPC() {
        List<IPC> lista = ipcRepositorio.findAll();
        return lista.stream().map(ipc -> {
            IPCRespuestaDTO dto = new IPCRespuestaDTO();
            dto.setFechaIPC(ipc.getFechaIPC());
            dto.setValorIPC(ipc.getValorIPC());
            return dto;
        }).toList();
    }

    /**
     * Busca el IPC registrado para un año específico.
     * @param anio Año a consultar.
     * @return DTO con el valor del IPC, o null si no existe.
     */
    @Override
    public IPCRespuestaDTO buscarIPCPorAnio(Integer anio) {
        int anioActual = Year.now().getValue();
        if(anio>anioActual) {
            throw new RuntimeException("No se puede consultar el IPC de años futuros");
        }
        IPC ipc = ipcRepositorio.findById(anio).orElse(null);
        if (ipc == null) return null;
        
        IPCRespuestaDTO dto = new IPCRespuestaDTO();
        dto.setFechaIPC(ipc.getFechaIPC());
        dto.setValorIPC(ipc.getValorIPC());
        return dto;
    }

    /**
     * Registra un nuevo valor de IPC.
     * Solo se permite registrar el IPC del año siguiente al último registrado.
     * @param peticion DTO con los datos del IPC a registrar.
     * @throws RuntimeException si faltan datos, si el año no es consecutivo o si ya existe el registro.
     */
    @Override
    public void registrarIPC(RegistroIPCPeticion peticion) {
        int anioActual = Year.now().getValue();

        if (peticion.getFechaIPC() == null || peticion.getValorIPC() == null) {
            throw new RuntimeException("Los campos fechaIPC y valorIPC son obligatorios");
        }

        if (peticion.getFechaIPC() != anioActual) {
            throw new RuntimeException("El año del IPC a registrar debe ser el año actual");
        }

        // Validar primero si ya existe un IPC para el año
        Optional<IPC> existente = ipcRepositorio.findByFechaIPC(peticion.getFechaIPC());
        if (existente.isPresent()) {
            throw new RuntimeException("Ya existe un registro de IPC para el año " + peticion.getFechaIPC());
        }

        // Obtener todos los IPCs registrados
        List<IPC> ipcList = ipcRepositorio.findAll();

        // Validar que el año a registrar sea el siguiente al último registrado
        if (!ipcList.isEmpty()) {
            int maxAnio = ipcList.stream().mapToInt(IPC::getFechaIPC).max().orElse(anioActual - 1);
            if (peticion.getFechaIPC() != maxAnio + 1) {
                throw new RuntimeException("Debe registrar primero el IPC del año inmediatamente anterior (último registrado: " + maxAnio + ")");
            }
        }

        IPC ipc = new IPC();
        ipc.setFechaIPC(peticion.getFechaIPC());
        ipc.setValorIPC(peticion.getValorIPC());
        ipcRepositorio.save(ipc);
    }



    /**
     * Actualiza el valor del IPC para el año dado.
     * @param anio Año del IPC a actualizar.
     * @param peticion DTO con el nuevo valor del IPC.
     * @throws RuntimeException si el valor es nulo, el año no es válido o el IPC no existe.
     */
    @Override
    public void actualizarIPC(Integer anio, RegistroIPCPeticion peticion) {
        int anioActual = Year.now().getValue();
        if (peticion.getValorIPC() == null) {
            throw new RuntimeException("El campo valorIPC es obligatorio");
        }
        // Solo se puede editar el IPC del año actual, el inmediatamente anterior o de años futuros
        /*if (anio < anioActual - 1) {
            throw new RuntimeException("Solo se puede actualizar el IPC de un año anterior al actual en adelante (desde " + (anioActual - 1) + ")");
        }*/
        Optional<IPC> existente = ipcRepositorio.findById(anio);
        if (existente.isEmpty()) {
            throw new RuntimeException("No existe un IPC registrado para el año especificado");
        }
        IPC ipcExistente = existente.get();
        if(ipcExistente.getFechaIPC() != anioActual) {
            throw new RuntimeException("No se puede actualizar el IPC de un año anterior al actual");
        }
        ipcExistente.setValorIPC(peticion.getValorIPC());
        ipcRepositorio.save(ipcExistente);
    }

    /**
     * Elimina el registro de IPC para el año dado.
     * Solo se permite eliminar el IPC del año actual o posteriores.
     * @param anio Año del IPC a eliminar.
     * @throws RuntimeException si el año es anterior al actual.
     */
    @Override
    public void eliminarIPC(Integer anio) {
        int anioActual = Year.now().getValue();
        if (anio < anioActual) {
            throw new RuntimeException("No se puede eliminar IPC de años anteriores al actual");
        }
        ipcRepositorio.deleteById(anio);
    }
}
