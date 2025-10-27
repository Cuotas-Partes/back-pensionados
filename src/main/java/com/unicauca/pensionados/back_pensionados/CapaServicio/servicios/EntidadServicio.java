package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroEntidadPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroTrabajoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadConPensionadosRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.TrabajoRespuesta;

import java.util.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

@Service
public class EntidadServicio implements IEntidadServicio {

    @Autowired
    private EntidadRepositorio entidadRepository;
    @Autowired
    private PensionadoRepositorio pensionadoRepositorio;
    @Autowired
    private TrabajoRepositorio trabajoRepositorio;
    @Autowired
    private CuotaParteServicio cuotaParteServicio;
    @Autowired
    private CuotaParteRepositorio cuotaParteRepositorio;
    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    /**
     * Registra una nueva entidad en la base de datos junto con sus pensionados y trabajos asociados.
     * 
     * @param request los datos de la entidad, pensionados y trabajos a registrar
     * @throws RuntimeException si ya existe una entidad con el mismo NIT o nombre
     * @throws Exception si ocurre un error al registrar la entidad
     */
    @Transactional
    @Override
    public void registrarEntidad(RegistroEntidadPeticion request) {
        if (entidadRepository.existsByNitEntidad(request.getNitEntidad())) {
            throw new RuntimeException("Ya existe una entidad con el NIT: " + request.getNitEntidad());
        }

        if (entidadRepository.existsByNombreEntidad(request.getNombreEntidad())) {
            throw new RuntimeException("Ya existe una entidad con el nombre: " + request.getNombreEntidad());
        }

        Entidad entidad = new Entidad();
        entidad.setNitEntidad(request.getNitEntidad());
        entidad.setNombreEntidad(request.getNombreEntidad());
        entidad.setDireccionEntidad(request.getDireccionEntidad());
        entidad.setTelefonoEntidad(request.getTelefonoEntidad());
        entidad.setEmailEntidad(request.getEmailEntidad());
        entidad.setEstadoEntidad(request.getEstadoEntidad());

        if (entidad.getTrabajos() == null) {
            entidad.setTrabajos(new ArrayList<>());
        }
        entidadRepository.save(entidad);

        if (request.getTrabajos() != null && !request.getTrabajos().isEmpty()) {
            for (RegistroTrabajoPeticion registroTrabajoPeticion : request.getTrabajos()) {
                Pensionado pensionado = pensionadoRepositorio.findById(registroTrabajoPeticion.getNumeroIdPersona())
                    .orElseThrow(() -> new RuntimeException(
                        "El pensionado con ID: " + registroTrabajoPeticion.getNumeroIdPersona() + " no está registrado"));

                Trabajo trabajo = new Trabajo();

               //trabajo.setId(trabajoId);
                trabajo.setDiasDeServicio(registroTrabajoPeticion.getDiasDeServicio());
                trabajo.setEntidad(entidad);
                trabajo.setPensionado(pensionado);
                trabajoRepositorio.save(trabajo);
                Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                    .stream()
                    .mapToLong(Trabajo::getDiasDeServicio)
                    .sum();
                pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
                pensionadoRepositorio.save(pensionado);

                cuotaParteServicio.registrarCuotaParte(trabajo);
                entidad.getTrabajos().add(trabajo);

                cuotaParteServicio.recalcularCuotasPartesPorPensionado(pensionado);
                
            }
        }
    }


    /**
     * Actualiza una entidad existente en la base de datos.
     * 
     * @param nid el NIT de la entidad a actualizar
     * @param entidad los nuevos datos de la entidad
     * @throws RuntimeException si no se encuentra la entidad
     * @throws Exception si ocurre un error al actualizar la entidad
     */
    @Transactional
    @Override
    public void actualizar(Long nid, RegistroEntidadPeticion entidad) {
        Entidad entidadExistente = entidadRepository.findById(nid)
            .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nid));

        if (entidadRepository.existsByNombreEntidad(entidad.getNombreEntidad())
            && !entidadExistente.getNombreEntidad().equals(entidad.getNombreEntidad())) {
            throw new RuntimeException("Ya existe una entidad con el nombre: " + entidad.getNombreEntidad());
        }

        entidadExistente.setNombreEntidad(entidad.getNombreEntidad());
        entidadExistente.setDireccionEntidad(entidad.getDireccionEntidad());
        entidadExistente.setTelefonoEntidad(entidad.getTelefonoEntidad());
        entidadExistente.setEmailEntidad(entidad.getEmailEntidad());
        entidadExistente.setEstadoEntidad(entidad.getEstadoEntidad());

        if (entidad.getTrabajos() != null) {
            List<Trabajo> trabajosActuales = trabajoRepositorio.findByEntidadNitEntidad(nid);
            Map<Long, Trabajo> mapaTrabajosActuales = trabajosActuales.stream()
                .collect(Collectors.toMap(trabajo -> trabajo.getPensionado().getNumeroIdPersona(), trabajo -> trabajo));

            for (RegistroTrabajoPeticion trabajoPeticion : entidad.getTrabajos()) {
                Long numeroIdPersona = trabajoPeticion.getNumeroIdPersona();
                Pensionado pensionado = pensionadoRepositorio.findById(numeroIdPersona)
                    .orElseThrow(() -> new RuntimeException(
                        "El pensionado con ID: " + numeroIdPersona + " no está registrado"));

                Trabajo trabajo = mapaTrabajosActuales.get(numeroIdPersona);
                if (trabajo != null) {
                    trabajo.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                    trabajoRepositorio.save(trabajo);
                    cuotaParteServicio.registrarCuotaParte(trabajo);
                    mapaTrabajosActuales.remove(numeroIdPersona);
                } else {
                    Trabajo nuevoTrabajo = new Trabajo();
                    nuevoTrabajo.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                    nuevoTrabajo.setEntidad(entidadExistente);
                    nuevoTrabajo.setPensionado(pensionado);
                    trabajoRepositorio.save(nuevoTrabajo);
                    cuotaParteServicio.registrarCuotaParte(nuevoTrabajo);
                    entidadExistente.getTrabajos().add(nuevoTrabajo);
                }

                Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                    .stream()
                    .mapToLong(Trabajo::getDiasDeServicio)
                    .sum();
                pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
                pensionadoRepositorio.save(pensionado);
            }
        }

        entidadRepository.save(entidadExistente);
    }

    /**
     * Edita la lista de pensionados asociados a una entidad.
     * Permite agregar nuevos pensionados, eliminar pensionados existentes o modificar los días de servicio.
     *
     * @param nitEntidad El NIT de la entidad a editar.
     * @param trabajosActualizados La lista actualizada de trabajos con los pensionados y sus días de servicio.
     * @throws RuntimeException Si la entidad no existe o si algún pensionado no está registrado.
     */
    @Transactional
    @Override
    public void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados) {
        Entidad entidad = entidadRepository.findById(nitEntidad)
                .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nitEntidad));

        List<Trabajo> trabajosActuales = trabajoRepositorio.findByEntidadNitEntidad(nitEntidad);
        Map<Long, Trabajo> mapaTrabajosActuales = trabajosActuales.stream()
                .collect(Collectors.toMap(trabajo -> trabajo.getPensionado().getNumeroIdPersona(), trabajo -> trabajo));

        for (RegistroTrabajoPeticion trabajoPeticion : trabajosActualizados) {
            Long numeroIdPersona = trabajoPeticion.getNumeroIdPersona();
            Pensionado pensionado = pensionadoRepositorio.findById(numeroIdPersona)
                    .orElseThrow(() -> new RuntimeException(
                            "El pensionado con ID: " + numeroIdPersona + " no está registrado"));

            Optional<Trabajo> trabajoExistenteOpt = trabajoRepositorio.findByPensionadoAndEntidad(pensionado, entidad);

            Trabajo trabajoModificado = null;
            if (trabajoExistenteOpt.isPresent()) {
                Trabajo trabajoExistente = trabajoExistenteOpt.get();
                if (trabajoPeticion.getDiasDeServicio() == 0) {
                    cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajoExistente.getIdTrabajo())
                        .ifPresent(cuotaParteRepositorio::delete);
                    trabajoRepositorio.delete(trabajoExistente);
                    entidad.getTrabajos().remove(trabajoExistente);

                    Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                            .stream()
                            .mapToLong(Trabajo::getDiasDeServicio)
                            .sum();
                    pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
                    pensionadoRepositorio.save(pensionado);

                    mapaTrabajosActuales.remove(numeroIdPersona);

                    boolean tieneMasTrabajosEnEntidad = trabajoRepositorio.findByPensionadoAndEntidad(pensionado, entidad).isPresent();
                    if (!tieneMasTrabajosEnEntidad) {
                        entidad.getTrabajos().removeIf(t -> t.getPensionado().getNumeroIdPersona().equals(numeroIdPersona));
                    }
                    continue; 
                } else {
                    trabajoExistente.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                    trabajoRepositorio.save(trabajoExistente);
                    trabajoModificado = trabajoExistente;
                    mapaTrabajosActuales.remove(numeroIdPersona);
                }
            } else if (trabajoPeticion.getDiasDeServicio() > 0) {
                Trabajo nuevoTrabajo = new Trabajo();
                nuevoTrabajo.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                nuevoTrabajo.setEntidad(entidad);
                nuevoTrabajo.setPensionado(pensionado);
                trabajoRepositorio.save(nuevoTrabajo);
                entidad.getTrabajos().add(nuevoTrabajo);
                trabajoModificado = nuevoTrabajo;
            }

            Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                    .stream()
                    .mapToLong(Trabajo::getDiasDeServicio)
                    .sum();
            pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
            pensionadoRepositorio.save(pensionado);
            cuotaParteServicio.recalcularCuotasPartesPorPensionado(pensionado);
            if (trabajoModificado != null && totalDiasTrabajo > 0) {
                cuotaParteServicio.registrarCuotaParte(trabajoModificado);
            }

            boolean tieneMasTrabajosEnEntidad = trabajoRepositorio.findByPensionadoAndEntidad(pensionado, entidad).isPresent();
            if (!tieneMasTrabajosEnEntidad) {
                entidad.getTrabajos().removeIf(t -> t.getPensionado().getNumeroIdPersona().equals(numeroIdPersona));
            }
        }

        // Eliminar los trabajos que no están en la lista actualizada
        for (Trabajo trabajoAEliminar : mapaTrabajosActuales.values()) {
            Pensionado pensionado = trabajoAEliminar.getPensionado();
            cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajoAEliminar.getIdTrabajo())
                .ifPresent(cuotaParteRepositorio::delete);
            trabajoRepositorio.delete(trabajoAEliminar);
            entidad.getTrabajos().remove(trabajoAEliminar);

            // actualizamos total de días de trabajo después de eliminar
            Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                    .stream()
                    .mapToLong(Trabajo::getDiasDeServicio)
                    .sum();
            pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
            pensionadoRepositorio.save(pensionado);

            boolean tieneMasTrabajosEnEntidad = trabajoRepositorio.findByPensionadoAndEntidad(pensionado, entidad).isPresent();
            if (!tieneMasTrabajosEnEntidad) {
                entidad.getTrabajos().removeIf(t -> t.getPensionado().getNumeroIdPersona().equals(pensionado.getNumeroIdPersona()));
            }
        }

        entidadRepository.save(entidad);
    }
    /**
     * Lista todas las entidades ordenadas por NIT ascendente.
     * 
     * @return una lista de objetos Entidad
     */
    @Override
    public List<EntidadConPensionadosRespuesta> listarTodos() {
        List<Entidad> entidades = entidadRepository.findAllByOrderByNitEntidadAsc();

        return entidades.stream().map(entidad -> {
            List<TrabajoRespuesta> trabajos = entidad.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                    .diasDeServicio(trabajo.getDiasDeServicio())
                    .nitEntidad(trabajo.getEntidad().getNitEntidad())
                    .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                    .idTrabajo(trabajo.getIdTrabajo())
                    .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                    .build())
                .toList();
                List<PensionadoRespuesta> pensionados = entidad.getTrabajos().stream()
                .map(Trabajo::getPensionado) // <-- tipo inferido: Pensionado
                .filter(Objects::nonNull)
                .distinct()
                .map((Pensionado p) -> PensionadoRespuesta.builder()
                    .numeroIdPersona(p.getNumeroIdPersona())
                    .tipoIdPersona(p.getTipoIdPersona())
                    .nombrePersona(p.getNombrePersona())
                    .apellidosPersona(p.getApellidosPersona())
                    .fechaNacimientoPersona(p.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(p.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(p.getEstadoPersona())
                    .generoPersona(p.getGeneroPersona())
                    //.fechaDefuncionPersona(p.getFechaDefuncionPersona())
                    .fechaInicioPension(p.getFechaInicioPension())
                    .valorInicialPension(p.getValorInicialPension())
                    .resolucionPension(p.getResolucionPension())
                    .entidadJubilacion(entidad.getNombreEntidad())
                    .totalDiasTrabajo(entidad.getTrabajos().stream()
                        .map(Trabajo::getDiasDeServicio)
                        .reduce(0L, Long::sum))
                    .diasDeServicio(trabajoRepositorio.findByPensionadoAndEntidad(p, entidad).get().getDiasDeServicio())
                    .nitEntidad(entidad.getNitEntidad())
                    .trabajos(p.getTrabajos().stream()
                        .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                            .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                            .nitEntidad(trabajo.getEntidad().getNitEntidad())
                            .idTrabajo(trabajo.getIdTrabajo())
                            .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                            .diasDeServicio(trabajo.getDiasDeServicio())
                            .build())
                        .toList())
                    .build())
                .toList();


            return EntidadConPensionadosRespuesta.builder()
                .nitEntidad(entidad.getNitEntidad())
                .nombreEntidad(entidad.getNombreEntidad())
                .direccionEntidad(entidad.getDireccionEntidad())
                .telefonoEntidad(entidad.getTelefonoEntidad())
                .emailEntidad(entidad.getEmailEntidad())
                .estadoEntidad(entidad.getEstadoEntidad())
                .trabajos(trabajos)
                .pensionados(pensionados)
                .build();
        }).toList();
    }

    /**
     * Busca una entidad por su NIT.
     * 
     * @param nit el NIT de la entidad a buscar
     * @return un objeto Entidad
     * @throws RuntimeException si no se encuentra la entidad
     */
    @Override
    public Entidad buscarPorNit(Long nit) {
        return entidadRepository.findById(nit)
                .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nit));
    }

    /**
     * Busca entidades por nombre, NIT o dirección.
     * 
     * @param query el criterio de búsqueda (nombre, NIT o dirección)
     * @return una lista de objetos Entidad
     */
      @Override
        public List<EntidadConPensionadosRespuesta> buscarEntidadesPorCriterio(String query) {
            List<Entidad> entidades = new ArrayList<>();

            // Buscar entidades por NIT o criterios de texto
            try {
                Long nit = Long.parseLong(query);
                entidades.addAll(entidadRepository.findByNitEntidadIs(nit));
            } catch (NumberFormatException e) {
                // Si no es un número, buscar por nombre, dirección o email
                entidades.addAll(entidadRepository.findByNombreEntidadContainingIgnoreCase(query));
                entidades.addAll(entidadRepository.findByDireccionEntidadContainingIgnoreCase(query));
                entidades.addAll(entidadRepository.findByEmailEntidadContainingIgnoreCase(query));
            }

            // Eliminar duplicados
            entidades = entidades.stream().distinct().toList();

            // Mapear las entidades a DTOs
            return entidades.stream().map(entidad -> {
                // Mapear los trabajos asociados a la entidad
                List<TrabajoRespuesta> trabajos = entidad.getTrabajos().stream()
                .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                    .idTrabajo(trabajo.getIdTrabajo())
                    .diasDeServicio(trabajo.getDiasDeServicio())
                    .nitEntidad(trabajo.getEntidad().getNitEntidad())
                    .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                    .build())
                .toList();
                
                List<PensionadoRespuesta> pensionados = entidad.getTrabajos().stream()
                .map(Trabajo::getPensionado) 
                .filter(Objects::nonNull)
                .distinct()
                .map((Pensionado p) -> PensionadoRespuesta.builder()
                    .numeroIdPersona(p.getNumeroIdPersona())
                    .tipoIdPersona(p.getTipoIdPersona())
                    .nombrePersona(p.getNombrePersona())
                    .apellidosPersona(p.getApellidosPersona())
                    .fechaNacimientoPersona(p.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(p.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(p.getEstadoPersona())
                    .generoPersona(p.getGeneroPersona())
                    //.fechaDefuncionPersona(p.getFechaDefuncionPersona())
                    .fechaInicioPension(p.getFechaInicioPension())
                    .valorInicialPension(p.getValorInicialPension())
                    .resolucionPension(p.getResolucionPension())
                    .entidadJubilacion(entidad.getNombreEntidad())
                    .nitEntidad(entidad.getNitEntidad())
                    .totalDiasTrabajo(entidad.getTrabajos().stream()
                        .map(Trabajo::getDiasDeServicio)
                        .reduce(0L, Long::sum))
                    .diasDeServicio(trabajoRepositorio.findByPensionadoAndEntidad(p, entidad).get().getDiasDeServicio())
                    .trabajos(p.getTrabajos().stream()
                        .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                            .nitEntidad(trabajo.getEntidad().getNitEntidad())
                            .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                            .idTrabajo(trabajo.getIdTrabajo())
                            .diasDeServicio(trabajo.getDiasDeServicio())
                            .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                            .build())
                        .toList())
                    .build())
                .toList();

                return EntidadConPensionadosRespuesta.builder()
                    .nitEntidad(entidad.getNitEntidad())
                    .nombreEntidad(entidad.getNombreEntidad())
                    .direccionEntidad(entidad.getDireccionEntidad())
                    .telefonoEntidad(entidad.getTelefonoEntidad())
                    .emailEntidad(entidad.getEmailEntidad())
                    .estadoEntidad(entidad.getEstadoEntidad())
                    .pensionados(pensionados)
                    .trabajos(trabajos)
                    .build();
            }).toList();
        }
     

    /**
     * Activa una entidad cambiando su estado a "Activa".
     * 
     * @param nid el NIT de la entidad a activar
     * @return true si la entidad fue activada, false si no existe
     */
    @Override
    public boolean activarEntidad(Long nid) {
        Optional<Entidad> entidadOptional = entidadRepository.findById(nid);

        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();
            entidad.setEstadoEntidad("Activa");
            entidadRepository.save(entidad);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Desactiva una entidad cambiando su estado a "No Activa".
     * 
     * @param nid el NIT de la entidad a desactivar
     * @return true si la entidad fue desactivada, false si no existe
     */
    @Override
    public boolean desactivarEntidad(Long nid) {
        Optional<Entidad> entidadOptional = entidadRepository.findById(nid);

        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();
            entidad.setEstadoEntidad("No Activa");
            entidadRepository.save(entidad);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Busca entidades por nombre.
     * 
     * @param nombre el nombre de la entidad a buscar
     * @return una lista de objetos Entidad
     */
    @Override
    public List<Entidad> buscarEntidadPorNombre(String nombre) {
        return entidadRepository.findByNombreEntidadContainingIgnoreCase(nombre);
    }    
}