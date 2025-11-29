package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.back_pensionados.CapaServicio.excepciones.RecursoNoEncontrado;
import com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces.ISMMLVServicio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.SMMLVHistorico;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.SMMLVHistoricoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.SMMLVDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.SMMLVDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SMMLVServicio implements ISMMLVServicio {

    private final SMMLVHistoricoRepositorio smmlvRepositorio;

    @Override
    @Transactional
    public SMMLVDTORespuesta crear(SMMLVDTOPeticion peticion) {
        SMMLVHistorico smmlv = new SMMLVHistorico();
        smmlv.setAno(peticion.getAno());
        smmlv.setValor(peticion.getValor());
        smmlv.setCreatedAt(LocalDateTime.now());

        smmlv = smmlvRepositorio.save(smmlv);
        return mapearARespuesta(smmlv);
    }

    @Override
    @Transactional
    public SMMLVDTORespuesta actualizar(Long id, SMMLVDTOPeticion peticion) {
        SMMLVHistorico smmlv = smmlvRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("SMMLV no encontrado"));

        smmlv.setAno(peticion.getAno());
        smmlv.setValor(peticion.getValor());
        smmlv.setUpdatedAt(LocalDateTime.now());

        smmlv = smmlvRepositorio.save(smmlv);
        return mapearARespuesta(smmlv);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!smmlvRepositorio.existsById(id)) {
            throw new RecursoNoEncontrado("SMMLV no encontrado");
        }
        smmlvRepositorio.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SMMLVDTORespuesta obtenerPorId(Long id) {
        SMMLVHistorico smmlv = smmlvRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("SMMLV no encontrado"));
        return mapearARespuesta(smmlv);
    }

    @Override
    @Transactional(readOnly = true)
    public SMMLVDTORespuesta obtenerPorAno(Integer ano) {
        SMMLVHistorico smmlv = smmlvRepositorio.findByAno(ano)
                .orElseThrow(() -> new RecursoNoEncontrado("SMMLV no encontrado para el año " + ano));
        return mapearARespuesta(smmlv);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SMMLVDTORespuesta> obtenerTodos() {
        return smmlvRepositorio.findAll().stream()
                .map(this::mapearARespuesta)
                .collect(Collectors.toList());
    }

    private SMMLVDTORespuesta mapearARespuesta(SMMLVHistorico smmlv) {
        SMMLVDTORespuesta respuesta = new SMMLVDTORespuesta();
        respuesta.setId(smmlv.getId());
        respuesta.setAno(smmlv.getAno());
        respuesta.setValor(smmlv.getValor());
        respuesta.setCreatedAt(smmlv.getCreatedAt());
        respuesta.setUpdatedAt(smmlv.getUpdatedAt());
        return respuesta;
    }
}
