package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.dto.request.dtf.DtfRequestDTO;
import com.unicauca.pensionados.backend.application.service.interfaces.IDTFServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.model.entity.DTF;
import com.unicauca.pensionados.backend.domain.model.entity.LogCambio;
import com.unicauca.pensionados.backend.domain.model.mappers.dtf.DtfMapper.DtfMapper;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.DTFRepositorio;
import com.unicauca.pensionados.backend.application.dto.response.dtf.DTFDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public DTFDTO guardarDTF(DtfRequestDTO dto) {

        if (dto.getPeriodo() == null || dto.getValor() == null) {
            throw new BusinessValidationException("Periodo y valor son obligatorios");
        }

        // Validar duplicado
        dtfRepositorio.findByPeriodo(dto.getPeriodo())
                .ifPresent(dtf -> {
                    throw new BusinessValidationException(
                            "Ya existe un DTF para el periodo " + dto.getPeriodo()
                    );
                });

        DTF nuevoDTF = DtfMapper.toEntity(dto);
        DTF guardado = dtfRepositorio.save(nuevoDTF);

        logCambioServicio.registrarCreacion(nombreEntidad, guardado);

        return DtfMapper.toResponseDTO(guardado);
    }



    @Override
    public DTFDTO actualizarDTF(Long id, DtfRequestDTO dto) {

        DTF dtf = dtfRepositorio.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe DTF con id " + id
                ));

        DTF estadoAnterior = new DTF();
        BeanUtils.copyProperties(dtf, estadoAnterior);

        if (dto.getPeriodo() != null && !dto.getPeriodo().equals(dtf.getPeriodo())) {

            dtfRepositorio.findByPeriodo(dto.getPeriodo())
                    .ifPresent(existente -> {
                        throw new BusinessValidationException(
                                "Ya existe un DTF para el periodo " + dto.getPeriodo()
                        );
                    });

            dtf.setPeriodo(dto.getPeriodo());
        }

        if (dto.getValor() != null) {
            dtf.setValor(BigDecimal.valueOf(dto.getValor()));
        }

        DTF actualizado = dtfRepositorio.save(dtf);

        logCambioServicio.registrarActualizacion(nombreEntidad, estadoAnterior, actualizado);

        return DtfMapper.toResponseDTO(actualizado);
    }


    @Override
    public void eliminarDTF(Long id) {

        DTF dtf = dtfRepositorio.findById(id)
                .orElseThrow(() -> new BusinessValidationException(
                        "No existe DTF con id " + id
                ));

        if (!dtf.isEstado()) {
            throw new BusinessValidationException("El DTF ya se encuentra inactivo");
        }

        dtf.setEstado(false);
        dtfRepositorio.save(dtf);

        logCambioServicio.registrarEliminacion(nombreEntidad, dtf);
    }


    @Override
    public DTFDTO obtenerDTFPorId(Long id) {
        DTF dtf = dtfRepositorio.findById(id).orElse(null);
        logCambioServicio.registrarConsulta(nombreEntidad);
        return dtf == null ? null : modelMapper.map(dtfRepositorio.findById(id), DTFDTO.class);
    }

    @Override
    public List<DTFDTO> obtenerPorPeriodo(String periodo) {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return dtfRepositorio.findByPeriodo(periodo)
                .stream()
                .map(DtfMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<DTFDTO> listarDTFs() {
        logCambioServicio.registrarConsulta(nombreEntidad);
        return dtfRepositorio.findByEstadoTrue()
                .stream()
                .map(DtfMapper::toResponseDTO)
                .toList();
    }
}
