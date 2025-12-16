package com.unicauca.pensionados.backend.application.service.interfaces;

import java.util.List;

import com.unicauca.pensionados.backend.application.dto.request.filtro.FiltroPagoPeticion;
import com.unicauca.pensionados.backend.application.dto.request.pago.PagoDTOPeticion;

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

