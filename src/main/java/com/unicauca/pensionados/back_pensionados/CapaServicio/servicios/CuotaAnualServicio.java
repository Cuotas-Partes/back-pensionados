package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaAnual;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaAnualRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaAnualDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuotaAnualServicio implements ICuotaAnualServicio{
    @Autowired
    private CuotaAnualRepositorio cuotaAnualRepositorio;
    @Autowired
    private org.modelmapper.ModelMapper modelMapper;

    @Override
    public CuotaAnualDTO guardarCuotaAnual(CuotaAnualDTO cuotaAnual) {
        try{
            CuotaAnual cuotaAnualToSave = modelMapper.map(cuotaAnual, CuotaAnual.class);

            cuotaAnualRepositorio.save(cuotaAnualToSave);
            return modelMapper.map(cuotaAnualToSave, CuotaAnualDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido guardar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public CuotaAnualDTO actualizarCuotaAnual(CuotaAnualDTO cuotaAnual) {
        try{
            Optional<CuotaAnual> cuotaAnualReq = cuotaAnualRepositorio.findById(cuotaAnual.getIdCuotaAnual());
            if (cuotaAnualReq.isEmpty()) throw new RuntimeException("La cuota anual con id " + cuotaAnual.getIdCuotaAnual() + " no existe");

            CuotaAnual cuotaAnualToUpdate = cuotaAnualReq.get();

            if (cuotaAnual.getAnio() != null) cuotaAnualToUpdate.setAnio(cuotaAnual.getAnio());
            if (cuotaAnual.getValorIpc() != null) cuotaAnualToUpdate.setValorIpc(cuotaAnual.getValorIpc());
            if (cuotaAnual.getUsuarioRegistra() != null) cuotaAnualToUpdate.setUsuarioRegistra(cuotaAnual.getUsuarioRegistra());
            if (cuotaAnual.getSalarioMinimoVigente() != null) cuotaAnualToUpdate.setSalarioMinimoVigente(cuotaAnual.getSalarioMinimoVigente());
            if (cuotaAnual.getFechaRegistro() != null) cuotaAnualToUpdate.setFechaRegistro(cuotaAnual.getFechaRegistro());
            if (cuotaAnual.getUVT() != null) cuotaAnualToUpdate.setUVT(cuotaAnual.getUVT());
            if (cuotaAnual.getTasaInteresMoraAnual() != null) cuotaAnualToUpdate.setTasaInteresMoraAnual(cuotaAnual.getTasaInteresMoraAnual());
            if (cuotaAnual.getPIncrementoPensionalAnual() != null) cuotaAnualToUpdate.setPIncrementoPensionalAnual(cuotaAnual.getPIncrementoPensionalAnual());

            cuotaAnualRepositorio.save(cuotaAnualToUpdate);
            return modelMapper.map(cuotaAnualToUpdate, CuotaAnualDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido actualizar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public void eliminarCuotaAnual(Long id) {
        try{
            cuotaAnualRepositorio.deleteById(id);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido eliminar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public CuotaAnualDTO obtenerCuotaAnualPorAnio(Long anio) {
        CuotaAnual cuotaAnual = cuotaAnualRepositorio.findByAnio(anio);
        return cuotaAnual == null ? null : modelMapper.map(cuotaAnual, CuotaAnualDTO.class);
    }

    @Override
    public CuotaAnualDTO obtenerCuotaAnualPorId(Long id) {
        CuotaAnual cuotaAnual = cuotaAnualRepositorio.findById(id).orElse(null);
        return cuotaAnual == null ? null : modelMapper.map(cuotaAnual, CuotaAnualDTO.class);
    }

    @Override
    public List<CuotaAnualDTO> listarCuotasAnuales() {
        return cuotaAnualRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, CuotaAnualDTO.class)).toList();
    }
}
