package com.unicauca.pensionados.backend.application.service;

import com.unicauca.pensionados.backend.application.dto.request.entidad.RegistroEntidadPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroTrabajoPeticion;
import com.unicauca.pensionados.backend.application.service.interfaces.IEntidadServicio;
import com.unicauca.pensionados.backend.application.service.interfaces.ILogCambioServicio;
import com.unicauca.pensionados.backend.domain.exception.BusinessValidationException;
import com.unicauca.pensionados.backend.domain.exception.RecursoNoEncontrado;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Persona;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoEntidad;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PersonaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EntidadServicio implements IEntidadServicio {

    private final String nombreEntidad = "ENTIDAD";

    @Autowired
    private EntidadRepositorio entidadRepository;
    @Autowired
    private PersonaRepositorio personaRepositorio;

    // TODO: esto debería moverse a un servicio separado (Trabajos/CuotasPartes) para no mezclar responsabilidades.
    @Autowired
    private PensionadoRepositorio pensionadoRepositorio;

    @Autowired
    private ILogCambioServicio logCambioService;

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
        if (entidadRepository.existsByNit(request.getNit())) {
            throw new BusinessValidationException("Ya existe una entidad con el NIT: " + request.getNit());
        }

        if (entidadRepository.existsByName(request.getName())) {
            throw new BusinessValidationException("Ya existe una entidad con el nombre: " + request.getName());
        }

        Entidad entidad = new Entidad();
        entidad.setNit(request.getNit());
        entidad.setName(request.getName());
        entidad.setAddress(request.getAddress());
        entidad.setPhone(request.getPhone());
        entidad.setEmail(request.getEmail());
        entidad.setResponsibleOfficer(request.getResponsibleOfficer());
        entidad.setOfficerPosition(request.getOfficerPosition());
        entidad.setEstado(request.getEstado() != null ? request.getEstado() : EstadoEntidad.Activo);

        if (request.getIdPersonaEncargado() != null) {
            Persona encargado = personaRepositorio.findById(request.getIdPersonaEncargado())
                    .orElseThrow(() -> new RecursoNoEncontrado(
                            "No se encontró la Persona encargada con ID: " + request.getIdPersonaEncargado()));
            entidad.setEncargado(encargado);
        }

        logCambioService.registrarCreacion(nombreEntidad, entidadRepository.save(entidad));
    }


    /**
     * PENDIENTE:
     * El codigo comentado es necesario que se pase a un servicio especializado por ejemplo "actualizarTrabajosDeEntidad"
     * Se debe crear un proceso separado para manejar la actualización masiva de trabajos y el recálculo de cuotas partes por entidad.
     * El proceso debe:
      *  - Permitir actualizar, agregar o mantener los trabajos asociados a los pensionados de la entidad.
      *  - No eliminar trabajos con días de servicio igual a cero.
      *  - Recalcular las cuotas partes asociadas a los trabajos modificados o nuevos.
      *  - Actualizar el total de días de servicio acumulados por cada pensionado.
      *  - Mantener la integridad referencial sin modificar los datos administrativos de la entidad.
      * Leer los trabajos actuales de la entidad (trabajoRepositorio.findByEntidadNitEntidad(nid)).
      * Recalcular las cuotas partes de cada trabajo o pensionado afectado.
      * Actualizar el total de días trabajados de cada pensionado.
      * Evitar modificar datos personales o de la entidad.
      * Ejecutarse en bloque (transaccionalmente), pero sin depender del servicio de actualización
      * de entidades.
       * Motivo:
       * El método actual `actualizar()` de EntidadServicio debe limitarse a los datos básicos de la entidad.
     * Actualiza una entidad existente en la base de datos.
     *
     * @param idEntidad el NIT de la entidad a actualizar
     * @param request los nuevos datos de la entidad
     * @throws RuntimeException si no se encuentra la entidad
     * @throws Exception si ocurre un error al actualizar la entidad
     * Se comenta codigo ya que la informacion que se debe actualizar es solo administrativa
     * la informacion relacionada con trabajos o cuotas partes de una entidad, haran parte de un proceso
     */
    @Transactional
    @Override
    public void actualizar(Long idEntidad, RegistroEntidadPeticion request) {
        Entidad entidadExistente = entidadRepository.findById(idEntidad)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la entidad con ID: " + idEntidad));

        // Validaciones de unicidad (solo cuando cambian)
        if (!entidadExistente.getNit().equalsIgnoreCase(request.getNit()) && entidadRepository.existsByNit(request.getNit())) {
            throw new BusinessValidationException("Ya existe una entidad con el NIT: " + request.getNit());
        }

        if (!entidadExistente.getName().equalsIgnoreCase(request.getName()) && entidadRepository.existsByName(request.getName())) {
            throw new BusinessValidationException("Ya existe una entidad con el nombre: " + request.getName());
        }

        Entidad entidadAntigua = new Entidad();
        BeanUtils.copyProperties(entidadExistente, entidadAntigua);

        entidadExistente.setNit(request.getNit());
        entidadExistente.setName(request.getName());
        entidadExistente.setAddress(request.getAddress());
        entidadExistente.setPhone(request.getPhone());
        entidadExistente.setEmail(request.getEmail());
        entidadExistente.setResponsibleOfficer(request.getResponsibleOfficer());
        entidadExistente.setOfficerPosition(request.getOfficerPosition());

        if (request.getEstado() != null) {
            entidadExistente.setEstado(request.getEstado());
        }

        if (request.getIdPersonaEncargado() != null) {
            Persona encargado = personaRepositorio.findById(request.getIdPersonaEncargado())
                    .orElseThrow(() -> new RecursoNoEncontrado(
                            "No se encontró la Persona encargada con ID: " + request.getIdPersonaEncargado()));
            entidadExistente.setEncargado(encargado);
        } else {
            entidadExistente.setEncargado(null);
        }

        logCambioService.registrarActualizacion(nombreEntidad, entidadAntigua, entidadRepository.save(entidadExistente));
    }

    /**
     * PENDIENTE: Se debe Realizar un proceso que:
     * Calcule o recalcule las cuotas partes asociadas a los pensionados de la entidad.
     *  - Obtenga y actualice el total de días de servicio de cada pensionado.
     *  - Evite eliminar trabajos o registros con días de servicio igual a cero.
     *  - Reemplace al método actual masivo "editarPensionadosDeEntidad", el cual realiza estas tareas
     *    pero mezcla lógica de mantenimiento institucional con lógica de cálculo.
     * Este Sercio Permite:
     * Edita la lista de pensionados asociados a una entidad.
     * Permite agregar nuevos pensionados, eliminar pensionados existentes o modificar los días de servicio.
     *
     * @param nitEntidad El NIT de la entidad a editar.
     * @param trabajosActualizados La lista actualizada de trabajos con los pensionados y sus días de servicio.
     * @throws RuntimeException Si la entidad no existe o si algún pensionado no está registrado.
     */


    /**
     * Busca una entidad por su NIT.
     * 
     * @param nit el NIT de la entidad a buscar
     * @return un objeto Entidad
     * @throws RuntimeException si no se encuentra la entidad
     */
    @Override
    public Entidad buscarPorNit(Long nit) {
        logCambioService.registrarConsulta(nombreEntidad);
        return entidadRepository.findByNit(nit.toString())
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la entidad con NIT: " + nit));

    }

    public Entidad buscarPorId(Long id) {
        logCambioService.registrarConsulta(nombreEntidad);
        return entidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la entidad con ID: " + id));
    }

    @Override
    public List<Entidad> listarTodos() {
        logCambioService.registrarConsulta(nombreEntidad);
        return entidadRepository.findAllByOrderByNitAsc();
    }

    /**
     * Busca entidades por nombre, NIT o dirección.
     * 
     * @param query el criterio de búsqueda (nombre, NIT o dirección)
     * @return una lista de objetos Entidad
     */
    @Override
    public List<Entidad> buscarEntidadesPorCriterio(String query) {
        logCambioService.registrarConsulta(nombreEntidad);
        List<Entidad> entidades = new ArrayList<>();

        // Buscar entidades por NIT
        if (query.chars().allMatch(Character::isDigit)) {
            entidadRepository.findByNit(query).ifPresent(entidades::add);
        }

        // Buscar por texto
        entidades.addAll(entidadRepository.findByNameContainingIgnoreCase(query));
        entidades.addAll(entidadRepository.findByAddressContainingIgnoreCase(query));
        entidades.addAll(entidadRepository.findByEmailContainingIgnoreCase(query));

        return entidades.stream().distinct().toList();
    }



    /**
     * Activa una entidad cambiando su estado a "Activa".
     * 
     * @param nid el NIT de la entidad a activar
     * @return true si la entidad fue activada, false si no existe
     */
    @Override
    public boolean activarEntidad(Long nid) {
        Optional<Entidad> entidadOptional = entidadRepository.findByNit(nid.toString());


        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();

            Entidad entidadAntigua = new Entidad();
            BeanUtils.copyProperties(entidad, entidadAntigua);

            entidad.setEstado(EstadoEntidad.Activo);

            logCambioService.registrarActualizacion(
                    nombreEntidad,
                    entidadAntigua,
                    entidadRepository.save(entidad)
            );

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
        Optional<Entidad> entidadOptional = entidadRepository.findByNit(nid.toString());


        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();
            Entidad  entidadAntigua = new Entidad();
            BeanUtils.copyProperties(entidad, entidadAntigua);

            entidad.setEstado(EstadoEntidad.NoActivo);

            logCambioService.registrarActualizacion(nombreEntidad, entidadAntigua,entidadRepository.save(entidad));
            return true;
        } else {
            return false;
        }
    }

    /*TODO: Implementar metodo editar*/
    @Override
    @Transactional
    public void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados) {
        // Validación de entrada
        if (nitEntidad == null) {
            throw new BusinessValidationException("El ID de la entidad no puede ser nulo");
        }

        if (trabajosActualizados == null || trabajosActualizados.isEmpty()) {
            throw new BusinessValidationException("La lista de trabajos no puede estar vacía");
        }

        // Verificar que la entidad existe
        Entidad entidad = entidadRepository.findById(nitEntidad)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la entidad con ID: " + nitEntidad));

        throw new UnsupportedOperationException(
                "Esta funcionalidad aún no está implementada. " +
                "Debe crearse un servicio especializado (TrabajoServicio/CuotaParteServicio) " +
                "que maneje la actualización masiva de trabajos y el recálculo de cuotas partes. " +
                "Ver comentarios PENDIENTE en el código para más detalles sobre los requerimientos."
        );
    }

    /**
     * Busca entidades por nombre.
     * 
     * @param nombre el nombre de la entidad a buscar
     * @return una lista de objetos Entidad
     */
    @Override
    public List<Entidad> buscarEntidadPorNombre(String nombre) {
        logCambioService.registrarConsulta(nombreEntidad);
        return entidadRepository.findByNameContainingIgnoreCase(nombre);
    }

    @Transactional
    @Override
    public void eliminar(Long idEntidad) {
        Entidad entidad = entidadRepository.findById(idEntidad)
                .orElseThrow(() -> new RecursoNoEncontrado("No se encontró la entidad con ID: " + idEntidad));

        entidadRepository.delete(entidad);
    }

    public List<Pensionado> listarPensionadosPorEntidad(Long idEntidad) {
    Entidad entidad = entidadRepository.findById(idEntidad)
            .orElseThrow(() -> new RuntimeException("No se encontró la entidad con ID: " + idEntidad));

    // TODO: Método getPensionados() ya no existe en Entidad
    return new ArrayList<>();
    }



}