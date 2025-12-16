package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.service.interfaces.IIPCServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.model.entity.IPC;
import com.unicauca.pensionados.backend.domain.model.entity.LogCambio;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoIPC;
import com.unicauca.pensionados.backend.domain.model.mappers.ipc.IpcMapper;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.IPCRepositorio;
import com.unicauca.pensionados.backend.application.dto.response.ipc.IPCRespuestaDTO;
import com.unicauca.pensionados.backend.application.dto.request.ipc.RegistroIPCPeticion;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "IPC";

    /**
     * Lista todos los registros de IPC existentes.
     * @return Lista de DTOs con los valores de IPC registrados.
     */
    @Override
    public List<IPCRespuestaDTO> listarIPC() {
        List<IPC> lista = ipcRepositorio.findByEstado(EstadoIPC.ACTIVO);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return lista.stream().map(IpcMapper::toIpcDTO).toList();
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
            throw new BusinessValidationException("No se puede consultar el IPC de años futuros");
        }
        IPC ipc = ipcRepositorio.findByYear(anio)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe un registro de IPC para el año " + anio
                ));
        logCambioServicio.registrarConsulta(nombreEntidad);
        return IpcMapper.toIpcDTO(ipc);
    }

    /**
     * Registra un nuevo valor de IPC.
     * Solo se permite registrar el IPC del año siguiente al último registrado.
     * @param peticion DTO con los datos del IPC a registrar.
     * @throws RuntimeException si faltan datos, si el año no es consecutivo o si ya existe el registro.
     */
    @Override
    public void registrarIPC(RegistroIPCPeticion peticion) {
        validarPeticion(peticion);
        validarNoDuplicado(peticion.getYear());
        validarSecuencia(peticion.getYear());

        IPC ipc = construirIPC(peticion);
        ipc = ipcRepositorio.save(ipc);
        logCambioServicio.registrarCreacion(nombreEntidad, ipc);
    }



    /**
     * Actualiza el valor del IPC para el año dado.
     * @param peticion Año del IPC a actualizar.
     * @param peticion DTO con el nuevo valor del IPC.
     * @throws RuntimeException si el valor es nulo, el año no es válido o el IPC no existe.
     */
    @Override
    public void actualizarIPC(Long id, RegistroIPCPeticion peticion) {

        if (id == null) {
            throw new BusinessValidationException("El id del IPC es obligatorio para actualizar");
        }

        IPC ipc = ipcRepositorio.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe un IPC con id " + id
                ));
        validarActualizacion(ipc, peticion);
        actualizarCampos(ipc, peticion);

        ipcRepositorio.save(ipc);
        logCambioServicio.registrarActualizacion(nombreEntidad, ipc, ipc);
    }


    /**
     * Elimina el registro de IPC para el año dado.
     * Solo se permite eliminar el IPC del año actual o posteriores.
     * @param id Año del IPC a eliminar.
     * @throws RuntimeException si el año es anterior al actual.
     */
    @Override
    public void eliminarIPC(Long id) {

        IPC ipc = ipcRepositorio.findByIdAndEstado(id, EstadoIPC.ACTIVO)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe un IPC activo con id " + id
                ));

        ipc.setEstado(EstadoIPC.INACTIVO);
        ipc.setUpdatedAt(LocalDate.now());

        ipcRepositorio.save(ipc);
        logCambioServicio.registrarEliminacion(nombreEntidad, ipc);
    }




    private IPC construirIPC(RegistroIPCPeticion peticion) {
        IPC ipc = new IPC();
        ipc.setYear(peticion.getYear());
        ipc.setIpc(peticion.getIpc());
        ipc.setResolution(peticion.getResolution());
        ipc.setResolutionDate(peticion.getResolutionDate());
        ipc.setResolutionDetails(peticion.getResolutionDetails());
        ipc.setUpdatedAt(LocalDate.now());
        return ipc;
    }

    private void validarPeticion(RegistroIPCPeticion peticion) {
        int anioActual = Year.now().getValue();

        if (peticion.getYear() == null || peticion.getIpc() == null) {
            throw new BusinessValidationException(
                    "Los campos year e ipc son obligatorios"
            );
        }

        if (!peticion.getYear().equals(anioActual)) {
            throw new BusinessValidationException(
                    "El IPC solo puede registrarse para el año actual"
            );
        }
    }


    private void validarNoDuplicado(Integer anio) {
        if (ipcRepositorio.findByYear(anio).isPresent()) {
            throw new BusinessValidationException(
                    "Ya existe un registro de IPC para el año " + anio
            );
        }
    }
    private void validarSecuencia(Integer anio) {
        Integer ultimoAnio = ipcRepositorio.obtenerUltimoAnioRegistrado();

        if (ultimoAnio != null && anio != ultimoAnio + 1) {
            throw new BusinessValidationException(
                    "Debe registrar primero el IPC del año " + ultimoAnio
            );
        }
    }

    private void validarActualizacion(IPC existente, RegistroIPCPeticion peticion) {

        if (peticion.getIpc() == null) {
            throw new BusinessValidationException("El valor del IPC es obligatorio");
        }

        // Regla de negocio: el año no se puede modificar
        if (peticion.getYear() != null &&
                !peticion.getYear().equals(existente.getYear())) {
            throw new BusinessValidationException("No está permitido modificar el año del IPC");
        }
    }
    private void actualizarCampos(IPC ipc, RegistroIPCPeticion peticion) {
        ipc.setIpc(peticion.getIpc());
        ipc.setResolution(peticion.getResolution());
        ipc.setResolutionDate(peticion.getResolutionDate());
        ipc.setResolutionDetails(peticion.getResolutionDetails());
        ipc.setUpdatedAt(LocalDate.now());
    }


}
