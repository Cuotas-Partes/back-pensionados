package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Deuda;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.DeudaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DeudaDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeudaServicio implements IDeudaServicio{

    @Autowired
    private DeudaRepositorio deudaRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DeudaDTO guardarDeuda(DeudaDTO deuda) {
        try{
            Deuda nuevaDeuda = modelMapper.map(deuda, Deuda.class);
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

            if (deuda.getTipoDeuda() != null) deudaToUpdate.setTipoDeuda(deuda.getTipoDeuda());
            if (deuda.getEstadoDeuda() != null) deudaToUpdate.setEstadoDeuda(deuda.getEstadoDeuda());
            if (deuda.getPersona() != null) deudaToUpdate.setPersona(deuda.getPersona());
            if (deuda.getMontoDeuda() != null) deudaToUpdate.setMontoDeuda(deuda.getMontoDeuda());
            if (deuda.getFechaVencimiento() != null) deudaToUpdate.setFechaVencimiento(deuda.getFechaVencimiento());
            if (deuda.getTasaInteresAplicada() != null) deudaToUpdate.setTasaInteresAplicada(deuda.getTasaInteresAplicada());
            if (deuda.getUsuarioRegistro() != null) deudaToUpdate.setUsuarioRegistro(deuda.getUsuarioRegistro());

            deudaRepositorio.save(deudaToUpdate);
            return modelMapper.map(deudaToUpdate, DeudaDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido actualizar la deuda" + e.getMessage());
        }
    }

    @Override
    public void eliminarDeuda(Long id) {
        try {
            deudaRepositorio.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido eliminar la deuda" + e.getMessage());
        }
    }

    @Override
    public DeudaDTO obtenerDeudaPorId(Long id) {
        Deuda deuda = deudaRepositorio.findById(id).orElse(null);
        return deuda == null ? null : modelMapper.map(deuda, DeudaDTO.class);
    }

    @Override
    public List<DeudaDTO> obtenerDeudasPorTipoEstadoPersona(Deuda.TipoDeuda tipoDeuda, Deuda.EstadoDeuda estadoDeuda, Long idPersona) {
        List<Deuda> deudas = deudaRepositorio.findByTipoEstadoPersona(tipoDeuda, estadoDeuda, idPersona);
        return deudas.stream().map(obj -> modelMapper.map(obj, DeudaDTO.class)).toList();
    }

    @Override
    public List<DeudaDTO> listarDeudas() {
        return deudaRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, DeudaDTO.class)).toList();
    }
}
