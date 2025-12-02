package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaAnual;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaAnualRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.CuotaAnualDTO;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private ILogCambioServicio logCambioServicio;
    private final String nombreEntidad = "CUOTA_ANUAL";

    @Override
    public CuotaAnualDTO guardarCuotaAnual(CuotaAnualDTO cuotaAnual) {
        try{
            CuotaAnual cuotaAnualToSave = modelMapper.map(cuotaAnual, CuotaAnual.class);

            logCambioServicio.registrarCreacion(nombreEntidad, cuotaAnualRepositorio.save(cuotaAnualToSave));
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
            CuotaAnual cuotaAnualAntigua = new CuotaAnual();
            CuotaAnual cuotaAnualToUpdate = cuotaAnualReq.get();
            BeanUtils.copyProperties(cuotaAnualToUpdate, cuotaAnualAntigua);

            if (cuotaAnual.getAnio() != null) cuotaAnualToUpdate.setAnio(cuotaAnual.getAnio());
            if (cuotaAnual.getValorIpc() != null) cuotaAnualToUpdate.setValorIpc(cuotaAnual.getValorIpc());
            if (cuotaAnual.getUsuarioRegistra() != null) cuotaAnualToUpdate.setUsuarioRegistra(cuotaAnual.getUsuarioRegistra());
            if (cuotaAnual.getSalarioMinimoVigente() != null) cuotaAnualToUpdate.setSalarioMinimoVigente(cuotaAnual.getSalarioMinimoVigente());
            if (cuotaAnual.getFechaRegistro() != null) cuotaAnualToUpdate.setFechaRegistro(cuotaAnual.getFechaRegistro());
            if (cuotaAnual.getUVT() != null) cuotaAnualToUpdate.setUVT(cuotaAnual.getUVT());
            if (cuotaAnual.getTasaInteresMoraAnual() != null) cuotaAnualToUpdate.setTasaInteresMoraAnual(cuotaAnual.getTasaInteresMoraAnual());
            if (cuotaAnual.getPIncrementoPensionalAnual() != null) cuotaAnualToUpdate.setPIncrementoPensionalAnual(cuotaAnual.getPIncrementoPensionalAnual());

            cuotaAnualToUpdate= cuotaAnualRepositorio.save(cuotaAnualToUpdate);
            logCambioServicio.registrarActualizacion(nombreEntidad, cuotaAnualAntigua, cuotaAnualToUpdate);
            return modelMapper.map(cuotaAnualToUpdate, CuotaAnualDTO.class);
        }catch (Exception e){
            throw new RuntimeException("No se ha podido actualizar la cuota anual" + e.getMessage());
        }
    }

    @Override
    public void eliminarCuotaAnual(Long id) {
        CuotaAnual cuotaAnual = cuotaAnualRepositorio.findById(id).orElseThrow(() -> new RuntimeException("La cuota anual no existe"));
        cuotaAnualRepositorio.deleteById(id);
        logCambioServicio.registrarEliminacion(nombreEntidad, cuotaAnual);
    }

    @Override
    public CuotaAnualDTO obtenerCuotaAnualPorAnio(Long anio) {
        CuotaAnual cuotaAnual = cuotaAnualRepositorio.findByAnio(anio);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return cuotaAnual == null ? null : modelMapper.map(cuotaAnual, CuotaAnualDTO.class);
    }

    @Override
    public CuotaAnualDTO obtenerCuotaAnualPorId(Long id) {
        CuotaAnual cuotaAnual = cuotaAnualRepositorio.findById(id).orElse(null);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return cuotaAnual == null ? null : modelMapper.map(cuotaAnual, CuotaAnualDTO.class);
    }

    @Override
    public List<CuotaAnualDTO> listarCuotasAnuales() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return cuotaAnualRepositorio.findAll().stream().map(obj -> modelMapper.map(obj, CuotaAnualDTO.class)).toList();
    }
}
