package com.unicauca.pensionados.backend.application.service;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.FiltroPagoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.PagoDTOPeticion;
import com.unicauca.pensionados.backend.application.dto.response.PagoDTORespuesta;
import com.unicauca.pensionados.backend.application.dto.response.ResumenPagoDTORespuesta;

public interface IPagoServicio {
    
    PagoDTORespuesta crear(PagoDTOPeticion peticion);
    
    PagoDTORespuesta actualizar(Long id, PagoDTOPeticion peticion);
    
    void eliminar(Long id);
    
    PagoDTORespuesta obtenerPorId(Long id);
    
    List<PagoDTORespuesta> obtenerTodos();
    
    List<PagoDTORespuesta> buscarConFiltros(FiltroPagoPeticion filtro);
    
    List<PagoDTORespuesta> obtenerPorEntidad(Long nitEntidad);
    
    List<PagoDTORespuesta> obtenerPorAnio(Integer anio);
    
    List<PagoDTORespuesta> obtenerPorEntidadYAnio(Long nitEntidad, Integer anio);
    
    List<ResumenPagoDTORespuesta> obtenerResumenAgrupado(FiltroPagoPeticion filtro);
}

