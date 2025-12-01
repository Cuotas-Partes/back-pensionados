package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.DTF;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.DTFRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.DTFDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DTFServicio implements IDTFServicio {

    @Autowired
    private DTFRepositorio dtfRepositorio;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public DTFDTO guardarDTF(DTFDTO dtf) {
        try{
            DTF nuevoDTF = modelMapper.map(dtf, DTF.class);
            nuevoDTF.setFechaRegistro(LocalDate.now().toString());
            dtfRepositorio.save(nuevoDTF);
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

            if (dtf.getValor() != null)  dtfToUpdateEntity.setValor(dtf.getValor());
            if (dtf.getAnio() != null) dtfToUpdateEntity.setAnio(dtf.getAnio());
            if (dtf.getMes() != null) dtfToUpdateEntity.setMes(dtf.getMes());
            if (dtf.getUsuario() != null) dtfToUpdateEntity.setUsuario(dtf.getUsuario());

            dtfRepositorio.save(dtfToUpdateEntity);
            return modelMapper.map(dtfToUpdate, DTFDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido actualizar el DTF" + e.getMessage());
        }
    }

    @Override
    public void eliminarDTF(Long id) {
        try {
            dtfRepositorio.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("No se ha podido eliminar el DTF" + e.getMessage());
        }
    }

    @Override
    public DTFDTO obtenerDTFPorId(Long id) {
        DTF dtf = dtfRepositorio.findById(id).orElse(null);
        return dtf == null ? null : modelMapper.map(dtfRepositorio.findById(id), DTFDTO.class);
    }

    @Override
    public List<DTFDTO> obtenerDTFPorMesAnio(Long mes, Long anio) {
        List<DTF> dtfs = dtfRepositorio.findByMesAndAnio(mes, anio);
        return dtfs.isEmpty() ? null : dtfs.stream().map(obj -> modelMapper.map(obj, DTFDTO.class)).toList();
    }

    @Override
    public List<DTFDTO> listarDTFs() {
        return dtfRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, DTFDTO.class)).toList();
    }
}
