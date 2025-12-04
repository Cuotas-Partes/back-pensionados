package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios.Interfaces;

import java.util.List;

import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.FiltroPagoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.PagoDTOPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PagoDTORespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.ResumenPagoDTORespuesta;

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

