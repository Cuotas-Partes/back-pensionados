package com.unicauca.pensionados.backend.domain.model.mappers.dtf.DtfMapper;

import com.unicauca.pensionados.backend.application.dto.request.dtf.DtfRequestDTO;
import com.unicauca.pensionados.backend.application.dto.response.dtf.DTFDTO;
import com.unicauca.pensionados.backend.domain.model.entity.DTF;

import java.math.BigDecimal;

public class DtfMapper {
    private DtfMapper() {}

    public static DTF toEntity(DtfRequestDTO dto) {
        DTF dtf = new DTF();
        dtf.setPeriodo(dto.getPeriodo());
        dtf.setValor(BigDecimal.valueOf(dto.getValor()));
        return dtf;
    }

    public static DTFDTO toResponseDTO(DTF entity) {
        return DTFDTO.builder()
                .idDtf(entity.getIdDtf())
                .periodo(entity.getPeriodo())
                .valor(entity.getValor().doubleValue())
                .estado(entity.isEstado())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
