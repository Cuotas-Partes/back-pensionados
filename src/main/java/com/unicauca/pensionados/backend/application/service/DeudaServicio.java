package com.unicauca.pensionados.backend.application.service;
import com.unicauca.pensionados.backend.application.service.interfaces.IDeudaServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.model.entity.Deuda;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.DeudaRepositorio;
import com.unicauca.pensionados.backend.application.dto.response.DeudaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeudaServicio implements IDeudaServicio {

    @Autowired
    private DeudaRepositorio deudaRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "DEUDA";

    @Override
    public DeudaDTO crearDeuda(DeudaDTO deuda) {
        try{
            Deuda nuevaDeuda = modelMapper.map(deuda, Deuda.class);
            logCambioServicio.registrarCreacion(nombreEntidad,deudaRepositorio.save(nuevaDeuda));
            if(nuevaDeuda.getMontoDeuda() <= 0) throw new BusinessValidationException("El monto de la deuda debe ser mayor a cero");
            Optional<Deuda> deudaExistente = deudaRepositorio.findByFechaVencimiento(nuevaDeuda.getFechaVencimiento());
            if(deudaExistente.isPresent()) throw new BusinessValidationException("Ya existe una deuda con la misma fecha de vencimiento: " + nuevaDeuda.getFechaVencimiento());
            deudaRepositorio.save(nuevaDeuda);
            return modelMapper.map(nuevaDeuda, DeudaDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido guardar la deuda" + e.getMessage());
        }
    }

    @Override
    public DeudaDTO actualizarDeuda(DeudaDTO deuda) {
        try {
            Optional<Deuda> deudaToUpdateRes = deudaRepositorio.findById(deuda.getIdDeuda());
            if (deudaToUpdateRes.isEmpty()) throw new RuntimeException("La deuda con id " + deuda.getIdDeuda() + " no existe");

            Deuda deudaToUpdate = deudaToUpdateRes.get();
            Deuda deudaAntigua = new Deuda();
            BeanUtils.copyProperties(deuda, deudaAntigua);

            if (deuda.getMontoDeuda() <= 0) throw new BusinessValidationException("El monto de la deuda debe ser mayor a cero");
            if (deuda.getTipoDeuda() != null) deudaToUpdate.setTipoDeuda(deuda.getTipoDeuda());
            if (deuda.getEstadoDeuda() != null) deudaToUpdate.setEstadoDeuda(deuda.getEstadoDeuda());
            if (deuda.getPersona() != null) deudaToUpdate.setPersona(deuda.getPersona());
            if (deuda.getMontoDeuda() != null) deudaToUpdate.setMontoDeuda(deuda.getMontoDeuda());
            if (deuda.getFechaVencimiento() != null) deudaToUpdate.setFechaVencimiento(deuda.getFechaVencimiento());
            if (deuda.getTasaInteresAplicada() != null) deudaToUpdate.setTasaInteresAplicada(deuda.getTasaInteresAplicada());
            if (deuda.getUsuarioRegistro() != null) deudaToUpdate.setUsuarioRegistro(deuda.getUsuarioRegistro());

            logCambioServicio.registrarActualizacion(nombreEntidad, deudaAntigua, deudaRepositorio.save(deudaToUpdate));
            return modelMapper.map(deudaToUpdate, DeudaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido actualizar la deuda" + e.getMessage());
        }
    }

    @Override
    public void eliminarDeuda(Long id) {

        Deuda deuda= deudaRepositorio.findById(id).orElseThrow(()->new RuntimeException("La deuda con id " + id + " no existe"));
        logCambioServicio.registrarEliminacion(nombreEntidad,deuda);
        deudaRepositorio.deleteById(id);
    }

    @Override
    public DeudaDTO obtenerDeudaPorId(Long id) {
        Deuda deuda = deudaRepositorio.findById(id).orElse(null);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return deuda == null ? null : modelMapper.map(deuda, DeudaDTO.class);
    }

    @Override
    public List<DeudaDTO> obtenerDeudasPorTipoEstadoPersona(Deuda.TipoDeuda tipoDeuda, Deuda.EstadoDeuda estadoDeuda, Long idPersona) {
        List<Deuda> deudas = deudaRepositorio.findByTipoEstadoPersona(tipoDeuda, estadoDeuda, idPersona);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return deudas.stream().map(obj -> modelMapper.map(obj, DeudaDTO.class)).toList();
    }

    @Override
    public List<DeudaDTO> listarDeudas() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return deudaRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, DeudaDTO.class)).toList();
    }
}
