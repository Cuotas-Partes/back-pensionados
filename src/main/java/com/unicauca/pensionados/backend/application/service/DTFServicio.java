package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.model.entity.DTF;
import com.unicauca.pensionados.backend.domain.model.entity.LogCambio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.DTFRepositorio;
import com.unicauca.pensionados.backend.application.dto.response.DTFDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DTFServicio implements IDTFServicio {

    @Autowired
    private DTFRepositorio dtfRepositorio;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "DTF";

    @Override
    public DTFDTO guardarDTF(DTFDTO dtf) {
        try{
            DTF nuevoDTF = modelMapper.map(dtf, DTF.class);
            Optional<DTF> dtfExistente = dtfRepositorio.findByMesAndAnio(nuevoDTF.getMes(), nuevoDTF.getAnio());
            if(dtfExistente.isPresent()) {
                LogCambio logCambio = new LogCambio();
                logCambio.setEntidad("DTF");
                logCambio.setAccion(LogCambio.Accion.CREAR);
                logCambio.setValorNuevo("Intento de crear DTF para mes " + nuevoDTF.getMes() + " y año " + nuevoDTF.getAnio() + " duplicado");
                logCambio.setFecha(LocalDateTime.now());
                throw new BusinessValidationException("Ya existe un DTF para el mes " + nuevoDTF.getMes() + " y año " + nuevoDTF.getAnio());
            }
            nuevoDTF.setFechaRegistro(LocalDate.now().toString());
            nuevoDTF = dtfRepositorio.save(nuevoDTF);
            logCambioServicio.registrarCreacion(nombreEntidad, nuevoDTF);
            return modelMapper.map(nuevoDTF, DTFDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido guardar el DTF" + e.getMessage());
        }
    }

    @Override
    public DTFDTO actualizarDTF(DTFDTO dtf) {
        try{
            Optional<DTF> dtfToUpdate = dtfRepositorio.findById(dtf.getIdDtf());
            if(dtfToUpdate.isEmpty()) throw new RuntimeException("El DTF con id " + dtf.getIdDtf() + " no existe");
            DTF dtfToUpdateEntity = dtfToUpdate.get();
            DTF dtfAntiguo = new DTF();
            BeanUtils.copyProperties(dtfToUpdateEntity, dtfAntiguo);

            if (dtf.getValor() != null)  dtfToUpdateEntity.setValor(dtf.getValor());
            if (dtf.getAnio() != null) dtfToUpdateEntity.setAnio(dtf.getAnio());
            if (dtf.getMes() != null) dtfToUpdateEntity.setMes(dtf.getMes());
            if (dtf.getUsuario() != null) dtfToUpdateEntity.setUsuario(dtf.getUsuario());

            dtfToUpdateEntity = dtfRepositorio.save(dtfToUpdateEntity);
            logCambioServicio.registrarActualizacion(nombreEntidad, dtfAntiguo, dtfToUpdateEntity);

            return modelMapper.map(dtfToUpdate, DTFDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido actualizar el DTF" + e.getMessage());
        }
    }

    @Override
    public void eliminarDTF(Long id) {
        DTF dtf = dtfRepositorio.findById(id).orElseThrow(() -> new RuntimeException("El DTF con id " + id + " no existe"));
        dtfRepositorio.deleteById(id);
        logCambioServicio.registrarEliminacion(nombreEntidad, dtf);
    }

    @Override
    public DTFDTO obtenerDTFPorId(Long id) {
        DTF dtf = dtfRepositorio.findById(id).orElse(null);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return dtf == null ? null : modelMapper.map(dtfRepositorio.findById(id), DTFDTO.class);
    }

    @Override
    public List<DTFDTO> obtenerDTFPorMesAnio(Long mes, Long anio) {
        List<DTF> dtfs = dtfRepositorio.findByMesOrAnio(mes, anio);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return dtfs.isEmpty() ? null : dtfs.stream().map(obj -> modelMapper.map(obj, DTFDTO.class)).toList();
    }

    @Override
    public List<DTFDTO> listarDTFs() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return dtfRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, DTFDTO.class)).toList();
    }
}
