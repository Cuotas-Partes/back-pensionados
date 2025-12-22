package com.unicauca.pensionados.backend.domain.model.mappers.smmlv;

import com.unicauca.pensionados.backend.application.dto.request.smmlv.SMMLVDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.smmlv.SMMLVDTORespuesta;
import com.unicauca.pensionados.backend.domain.model.entity.SMMLVHistorico;

public class SmmlvMapper {
    private SmmlvMapper() {}

    public static SMMLVHistorico toEntity(SMMLVDTOPeticion dto) {
        SMMLVHistorico smmlv = new SMMLVHistorico();
        smmlv.setAno(dto.getAno());
        smmlv.setValor(dto.getValor());
        return smmlv;
    }

    public static SMMLVDTORespuesta toResponseDTO(SMMLVHistorico entity) {
        return SMMLVDTORespuesta.builder()
                .id(entity.getId())
                .ano(entity.getAno())
                .valor(entity.getValor())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
