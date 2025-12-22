package com.unicauca.pensionados.backend.application.service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.model.mappers.smmlv.SmmlvMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.application.service.interfaces.ISMMLVServicio;
import com.unicauca.pensionados.backend.domain.model.entity.SMMLVHistorico;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.SMMLVHistoricoRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.smmlv.SMMLVDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.smmlv.SMMLVDTORespuesta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SMMLVServicio implements ISMMLVServicio {

    @Autowired
    private SMMLVHistoricoRepositorio smmlvRepositorio;

    @Autowired
    private ILogCambioServicio logCambioServicio;

    private final String nombreEntidad = "SMMLV";

    @Override
    public SMMLVDTORespuesta registrarSMMLV(SMMLVDTOPeticion dto) {

        validarDto(dto);

        smmlvRepositorio.findByAno(dto.getAno())
                .ifPresent(smmlv -> {
                    throw new BusinessValidationException(
                            "Ya existe un SMMLV registrado para el año " + dto.getAno()
                    );
                });

        SMMLVHistorico nuevo = SmmlvMapper.toEntity(dto);
        SMMLVHistorico guardado = smmlvRepositorio.save(nuevo);

        logCambioServicio.registrarCreacion(nombreEntidad, guardado);

        return SmmlvMapper.toResponseDTO(guardado);
    }

    @Override
    public SMMLVDTORespuesta actualizarSMMLV(Long id, SMMLVDTOPeticion dto) {

        SMMLVHistorico existente = smmlvRepositorio.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe SMMLV con id " + id
                ));

        SMMLVHistorico estadoAnterior = new SMMLVHistorico();
        BeanUtils.copyProperties(existente, estadoAnterior);

        if (dto.getAno() != null && !dto.getAno().equals(existente.getAno())) {

            smmlvRepositorio.findByAno(dto.getAno())
                    .ifPresent(smmlv -> {
                        throw new BusinessValidationException(
                                "Ya existe un SMMLV para el año " + dto.getAno()
                        );
                    });

            existente.setAno(dto.getAno());
        }

        if (dto.getValor() != null) {
            existente.setValor(dto.getValor());
        }

        SMMLVHistorico actualizado = smmlvRepositorio.save(existente);

        logCambioServicio.registrarActualizacion(
                nombreEntidad,
                estadoAnterior,
                actualizado
        );

        return SmmlvMapper.toResponseDTO(actualizado);
    }

    @Override
    public void eliminarSMMLV(Long id) {

        SMMLVHistorico smmlv = smmlvRepositorio.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe SMMLV con id " + id
                ));

        smmlv.setEstado(false);
        logCambioServicio.registrarEliminacion(nombreEntidad, smmlv);
    }

    @Override
    public SMMLVDTORespuesta obtenerPorId(Long id) {

        SMMLVHistorico smmlv = smmlvRepositorio.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe SMMLV con id " + id
                ));

        logCambioServicio.registrarConsulta(nombreEntidad);

        return SmmlvMapper.toResponseDTO(smmlv);
    }

    @Override
    public SMMLVDTORespuesta obtenerPorAno(Integer ano) {

        SMMLVHistorico smmlv = smmlvRepositorio.findByAno(ano)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe SMMLV para el año " + ano
                ));

        logCambioServicio.registrarConsulta(nombreEntidad);

        return SmmlvMapper.toResponseDTO(smmlv);
    }

    @Override
    public List<SMMLVDTORespuesta> listarSMMLV() {

        logCambioServicio.registrarConsulta(nombreEntidad);

        return smmlvRepositorio.findAll()
                .stream()
                .map(SmmlvMapper::toResponseDTO)
                .toList();
    }

    // =========================
    // VALIDACIONES
    // =========================
    private void validarDto(SMMLVDTOPeticion dto) {

        if (dto.getAno() == null || dto.getValor() == null) {
            throw new BusinessValidationException("Año y valor son obligatorios");
        }

        int anoActual = Year.now().getValue();

        if (dto.getAno() < 1900 || dto.getAno() > anoActual) {
            throw new BusinessValidationException("El año no es válido");
        }

        if (dto.getValor().signum() <= 0) {
            throw new BusinessValidationException("El valor debe ser mayor a cero");
        }
    }
}
