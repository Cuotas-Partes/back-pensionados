package com.unicauca.pensionados.backend.application.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.backend.domain.model.enums.EstadoEntidad;
import com.unicauca.pensionados.backend.domain.model.entity.Entidad;
import com.unicauca.pensionados.backend.domain.model.entity.Pensionado;
import com.unicauca.pensionados.backend.domain.model.entity.Trabajo;
import com.unicauca.pensionados.backend.domain.model.enums.TipoIdentificacion;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.CuotaParteRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.EntidadRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PensionadoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.PeriodoRepositorio;
import com.unicauca.pensionados.backend.infrastructure.persistence.repository.TrabajoRepositorio;
import com.unicauca.pensionados.backend.application.dto.request.RegistroEntidadPeticion;
import com.unicauca.pensionados.backend.application.dto.request.RegistroTrabajoPeticion;
import com.unicauca.pensionados.backend.application.dto.response.EntidadConPensionadosRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.PensionadoRespuesta;
import com.unicauca.pensionados.backend.application.dto.response.TrabajoRespuesta;

import java.util.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;

@Service
public class EntidadServicio implements IEntidadServicio {

    private final String nombreEntidad = "ENTIDAD";

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
        if (entidadRepository.existsByNit(request.getNitEntidad().toString())) {
            throw new RuntimeException("Ya existe una entidad con el NIT: " + request.getNitEntidad());
        }

        if (entidadRepository.existsByName(request.getNombreEntidad())) {
            throw new RuntimeException("Ya existe una entidad con el nombre: " + request.getNombreEntidad());
        }

        Entidad entidad = new Entidad();
        entidad.setNit(request.getNitEntidad().toString());
        entidad.setName(request.getNombreEntidad());
        entidad.setAddress(request.getDireccionEntidad());
        entidad.setPhone(request.getTelefonoEntidad().toString());
        entidad.setEmail(request.getEmailEntidad());
        entidad.setEstado(request.getEstadoEntidad());

        //Guardar log
        logCambioService.registrarCreacion(nombreEntidad,entidadRepository.save(entidad));


        if (request.getTrabajos() != null && !request.getTrabajos().isEmpty()) {
            for (RegistroTrabajoPeticion registroTrabajoPeticion : request.getTrabajos()) {
                // Se busca al pensionado por su número de identificación (cédula)
                Pensionado pensionado = pensionadoRepositorio.findByCedula(
                    registroTrabajoPeticion.getNumeroIdentificacion().toString())
                    .orElseThrow(() -> new RuntimeException(
                        "El pensionado con cédula " + registroTrabajoPeticion.getNumeroIdentificacion() +
                        " no está registrado"));

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
                pensionado.setDiasTotalesTrabajados(totalDiasTrabajo.intValue());
                pensionadoRepositorio.save(pensionado);

                cuotaParteServicio.registrarCuotaParte(trabajo);

                cuotaParteServicio.recalcularCuotasPartesPorPensionado(pensionado);
                
            }
        }
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
     * @param entidad los nuevos datos de la entidad
     * @throws RuntimeException si no se encuentra la entidad
     * @throws Exception si ocurre un error al actualizar la entidad
     * Se comenta codigo ya que la informacion que se debe actualizar es solo administrativa
     * la informacion relacionada con trabajos o cuotas partes de una entidad, haran parte de un proceso
     */
    @Transactional
    @Override
    public void actualizar(Long idEntidad, RegistroEntidadPeticion entidad) {
    Entidad entidadExistente = entidadRepository.findById(idEntidad)
        .orElseThrow(() -> new RuntimeException("No se encontró la entidad con ID: " + idEntidad));

    if (entidadRepository.existsByName(entidad.getNombreEntidad())
        && !entidadExistente.getName().equals(entidad.getNombreEntidad())) {
        throw new RuntimeException("Ya existe una entidad con el nombre: " + entidad.getNombreEntidad());
    }

    Entidad entidadAntigua = new Entidad();
    BeanUtils.copyProperties(entidadExistente, entidadAntigua);

    entidadExistente.setName(entidad.getNombreEntidad());
    entidadExistente.setAddress(entidad.getDireccionEntidad());
    entidadExistente.setPhone(entidad.getTelefonoEntidad().toString());
    entidadExistente.setEmail(entidad.getEmailEntidad());
    entidadExistente.setEstado(entidad.getEstadoEntidad());
    /** Se comenta este codigo, ya que la actualizacion de una entidad no debe modificar trabajos o cuotas partes
    if (entidad.getTrabajos() != null) {
        List<Trabajo> trabajosActuales = trabajoRepositorio.findByEntidadNitEntidad(nid);
        
        // ==================== CORRECCIÓN 1 ====================
        // La clave del mapa ahora es el ID primario del pensionado.
        Map<Long, Trabajo> mapaTrabajosActuales = trabajosActuales.stream()
            .collect(Collectors.toMap(trabajo -> trabajo.getPensionado().getIdPersona(), trabajo -> trabajo));

        for (RegistroTrabajoPeticion trabajoPeticion : entidad.getTrabajos()) {
            
            // ==================== CORRECCIÓN 2 ====================
            // Se busca al pensionado usando su tipo y número de identificación.
            // Se busca al pensionado usando su número de identificación (cédula)
            Long numeroIdentificacion = trabajoPeticion.getNumeroIdentificacion();
            Pensionado pensionado = pensionadoRepositorio.findByCedula(numeroIdentificacion.toString())
                .orElseThrow(() -> new RuntimeException(
                    "El pensionado con cédula " + numeroIdentificacion + " no está registrado"));

            // Obtenemos el ID primario para trabajar con el mapa.
            Long idPersona = pensionado.getIdPersona();
            Trabajo trabajo = mapaTrabajosActuales.get(idPersona);

            if (trabajo != null) {
                trabajo.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                trabajoRepositorio.save(trabajo);
                cuotaParteServicio.registrarCuotaParte(trabajo);
                mapaTrabajosActuales.remove(idPersona); // Se usa el ID primario para remover.
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
            pensionado.setDiasTotalesTrabajados(totalDiasTrabajo.intValue());
            pensionadoRepositorio.save(pensionado);
            }
        }

        */
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

    /*==============================================================*/
    /**
    * Este método se utiliza únicamente para procesos masivos de actualización o importación de datos
    * (por ejemplo, lectura desde Excel o CSV).
    * 
    * No debe usarse desde la interfaz de usuario ni en operaciones individuales, ya que
    * puede eliminar trabajos y cuotas partes con días de servicio igual a cero.
    /* Actualiza los trabajos o Dias de Servicio, asociados a una entidad especifica por NIT
    Este Metodo Solo debe usar se de manera interna, es decir no se debe acceder mediante frontend
    Es recomendable que este metodo se invoque unicamente para realizar importacion de archivos
    ya que no tendra en cuenta los trabajos ingresados con dias de servicio en cero
    1️⃣ Buscar la entidad por su NIT.
    2️⃣ Obtener todos los trabajos actuales asociados a esa entidad.
    3️⃣ Crear un mapa (para detectar qué trabajos se eliminarán al final).
    4️⃣ Recorrer la lista nueva de trabajos que viene en la petición.
        - Buscar el pensionado correspondiente.
        - Si ya tenía un trabajo en esa entidad → actualizar o eliminar.
        - Si no lo tenía → crear uno nuevo.
        - Actualizar el total de días de servicio del pensionado.
        - Guardar el pensionado.
        - (Y aquí llama a registrar o recalcular cuotas partes)
    5️⃣ Al final, eliminar los trabajos que ya no estén en la lista actualizada.
    6️⃣ Guardar la entidad actualizada.

    /*==============================================================*/
    @Transactional
    @Override
    public void editarPensionadosDeEntidad(Long nitEntidad, List<RegistroTrabajoPeticion> trabajosActualizados) {
        Entidad entidad = entidadRepository.findByNit(nitEntidad.toString())
        .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nitEntidad));


        List<Trabajo> trabajosActuales = trabajoRepositorio.findByEntidadNit(nitEntidad.toString());

        // ==================== CORRECCIÓN 1: Clave del Mapa ====================
        // La clave del mapa debe ser el ID primario único del pensionado.
        Map<Long, Trabajo> mapaTrabajosActuales = trabajosActuales.stream()
                .collect(Collectors.toMap(trabajo -> trabajo.getPensionado().getIdPersona(), trabajo -> trabajo));

        for (RegistroTrabajoPeticion trabajoPeticion : trabajosActualizados) {
            
            // Se busca al pensionado usando su número de identificación (cédula)
            Long numeroIdentificacion = trabajoPeticion.getNumeroIdentificacion();
            Pensionado pensionado = pensionadoRepositorio.findByCedula(numeroIdentificacion.toString())
                    .orElseThrow(() -> new RuntimeException(
                            "El pensionado con cédula " + numeroIdentificacion + " no está registrado"));

            // Guardamos el ID primario para usarlo consistentemente
            Long idPersona = pensionado.getIdPersona();

            Optional<Trabajo> trabajoExistenteOpt = trabajoRepositorio.findByPensionadoAndEntidad(pensionado, entidad);

            Trabajo trabajoModificado = null;
            if (trabajoExistenteOpt.isPresent()) {
                // ✅ Significa que ya existe un trabajo entre ese pensionado y esa entidad
                Trabajo trabajoExistente = trabajoExistenteOpt.get();
                if (trabajoPeticion.getDiasDeServicio() == 0) {
                    cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajoExistente.getIdTrabajo())
                        .ifPresent(cuotaParteRepositorio::delete);
                    trabajoRepositorio.delete(trabajoExistente);

                    Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                            .stream()
                            .mapToLong(Trabajo::getDiasDeServicio)
                            .sum();
                    pensionado.setDiasTotalesTrabajados(totalDiasTrabajo.intValue());
                    pensionadoRepositorio.save(pensionado);
                    
                    // ==================== CORRECCIÓN 3: Remover del Mapa ====================
                    mapaTrabajosActuales.remove(idPersona);

                    continue;
                } else {
                    trabajoExistente.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                    trabajoRepositorio.save(trabajoExistente);
                    trabajoModificado = trabajoExistente;
                    
                    // ==================== CORRECCIÓN 5: Remover del Mapa ====================
                    mapaTrabajosActuales.remove(idPersona);
                }
            } else if (trabajoPeticion.getDiasDeServicio() > 0) {
                Trabajo nuevoTrabajo = new Trabajo();
                nuevoTrabajo.setDiasDeServicio(trabajoPeticion.getDiasDeServicio());
                nuevoTrabajo.setEntidad(entidad);
                nuevoTrabajo.setPensionado(pensionado);
                trabajoRepositorio.save(nuevoTrabajo);
                trabajoModificado = nuevoTrabajo;
            }

            Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                    .stream()
                    .mapToLong(Trabajo::getDiasDeServicio)
                    .sum();
            pensionado.setDiasTotalesTrabajados(totalDiasTrabajo.intValue());
            pensionadoRepositorio.save(pensionado);
            cuotaParteServicio.recalcularCuotasPartesPorPensionado(pensionado);
            if (trabajoModificado != null && totalDiasTrabajo > 0) {
                cuotaParteServicio.registrarCuotaParte(trabajoModificado);
            }
        }

        // Eliminar los trabajos que no están en la lista actualizada
        for (Trabajo trabajoAEliminar : mapaTrabajosActuales.values()) {
            Pensionado pensionado = trabajoAEliminar.getPensionado();
            cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajoAEliminar.getIdTrabajo())
                .ifPresent(cuotaParteRepositorio::delete);
            trabajoRepositorio.delete(trabajoAEliminar);

            // actualizamos total de días de trabajo después de eliminar
            Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                    .stream()
                    .mapToLong(Trabajo::getDiasDeServicio)
                    .sum();
            pensionado.setDiasTotalesTrabajados(totalDiasTrabajo.intValue());
            pensionadoRepositorio.save(pensionado);
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
        //Registrar log
        logCambioService.registrarConsulta(nombreEntidad);
        List<Entidad> entidades = entidadRepository.findAllByOrderByNitAsc();
        return entidades.stream().map(entidad -> {
            
            // Obtener trabajos de esta entidad desde el repositorio
            List<Trabajo> trabajosEntidad = trabajoRepositorio.findByEntidadNit(entidad.getNit());

            // Mapeo de Trabajos
            List<TrabajoRespuesta> trabajos = trabajosEntidad.stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                    .diasDeServicio(trabajo.getDiasDeServicio())
                    .nitEntidad(Long.parseLong(trabajo.getEntidad().getNit()))
                    .idPersona(trabajo.getPensionado().getIdPersona())
                    .idTrabajo(trabajo.getIdTrabajo())
                    .entidadJubilacion(trabajo.getEntidad().getName())
                    .build())
                .toList();

            // Mapeo de Pensionados desde los trabajos
            List<PensionadoRespuesta> pensionados = trabajosEntidad.stream()
                .map(Trabajo::getPensionado)
                .filter(Objects::nonNull)
                .distinct()
                .map((Pensionado p) -> PensionadoRespuesta.builder()
                    .idPersona(p.getIdPersona())
                    .numeroIdentificacion(Long.parseLong(p.getCedula()))
                    .tipoIdentificacion(null) // No existe en entidad
                    .nombrePersona(p.getNombre())
                    .apellidosPersona(p.getApellidos())
                    .estadoCivil(null) // No existe en entidad
                    .fechaNacimientoPersona(p.getFechaNacimiento())
                    .fechaExpedicionDocumentoIdPersona(p.getFechaExpedicionCedula())
                    .estadoPersona(p.getEstado())
                    .generoPersona(null) // No existe en entidad
                    .fechaDefuncionPersona(p.getFechaFallecimiento())
                    .fechaInicioPension(null) // No existe en entidad
                    .valorInicialPension(p.getValorPension())
                    .resolucionPension(null) // No existe en entidad
                    .entidadJubilacion(entidad.getName())
                    .totalDiasTrabajo(Long.valueOf(p.getDiasTotalesTrabajados()))
                    .diasDeServicio(trabajoRepositorio.findByPensionadoAndEntidad(p, entidad).map(Trabajo::getDiasDeServicio).orElse(0L))
                    .nitEntidad(Long.parseLong(entidad.getNit()))
                    .trabajos(new ArrayList<>()) // Sin relación directa
                    .build())
                .toList();

            return EntidadConPensionadosRespuesta.builder()
                .nitEntidad(Long.parseLong(entidad.getNit()))
                .nombreEntidad(entidad.getName())
                .direccionEntidad(entidad.getAddress())
                .telefonoEntidad(Long.valueOf(entidad.getPhone()))
                .emailEntidad(entidad.getEmail())
                .estadoEntidad(entidad.getEstado())
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
        logCambioService.registrarConsulta(nombreEntidad);
        return entidadRepository.findByNit(nit.toString())
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
        logCambioService.registrarConsulta(nombreEntidad);

        // Buscar entidades por NIT o criterios de texto
        try {
            Long nit = Long.parseLong(query);
            entidadRepository.findByNit(nit.toString()).ifPresent(entidades::add);
        } catch (NumberFormatException e) {
            // Si no es un número, buscar por nombre, dirección o email
            entidades.addAll(entidadRepository.findByNameContainingIgnoreCase(query));
            entidades.addAll(entidadRepository.findByAddressContainingIgnoreCase(query));
            entidades.addAll(entidadRepository.findByEmailContainingIgnoreCase(query));
        }

        // Eliminar duplicados
        entidades = entidades.stream().distinct().toList();

        // Mapear las entidades a DTOs
        return entidades.stream().map(entidad -> {
            // Obtener trabajos de esta entidad desde el repositorio
            List<Trabajo> trabajosEntidad = trabajoRepositorio.findByEntidadNit(entidad.getNit());

            // Mapeo de Trabajos
            List<TrabajoRespuesta> trabajos = trabajosEntidad.stream()
                .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                    .idTrabajo(trabajo.getIdTrabajo())
                    .diasDeServicio(trabajo.getDiasDeServicio())
                    .nitEntidad(Long.parseLong(trabajo.getEntidad().getNit()))
                    .idPersona(trabajo.getPensionado().getIdPersona())
                    .entidadJubilacion(trabajo.getEntidad().getName())
                    .build())
                .toList();
            
            // Mapeo de Pensionados desde los trabajos
            List<PensionadoRespuesta> pensionados = trabajosEntidad.stream()
                .map(Trabajo::getPensionado)
                .filter(Objects::nonNull)
                .distinct()
                .map((Pensionado p) -> PensionadoRespuesta.builder()
                    .idPersona(p.getIdPersona())
                    .numeroIdentificacion(Long.parseLong(p.getCedula()))
                    .tipoIdentificacion(null)
                    .nombrePersona(p.getNombre())
                    .apellidosPersona(p.getApellidos())
                    .estadoCivil(null)
                    .fechaNacimientoPersona(p.getFechaNacimiento())
                    .fechaExpedicionDocumentoIdPersona(p.getFechaExpedicionCedula())
                    .estadoPersona(p.getEstado())
                    .generoPersona(null)
                    .fechaDefuncionPersona(p.getFechaFallecimiento())
                    .fechaInicioPension(null)
                    .valorInicialPension(p.getValorPension())
                    .resolucionPension(null)
                    .entidadJubilacion(entidad.getName())
                    .nitEntidad(Long.parseLong(entidad.getNit()))
                    .totalDiasTrabajo(Long.valueOf(p.getDiasTotalesTrabajados()))
                    .diasDeServicio(trabajoRepositorio.findByPensionadoAndEntidad(p, entidad).map(Trabajo::getDiasDeServicio).orElse(0L))
                    .trabajos(new ArrayList<>())
                    .build())
                .toList();

            return EntidadConPensionadosRespuesta.builder()
                .nitEntidad(Long.parseLong(entidad.getNit()))
                .nombreEntidad(entidad.getName())
                .direccionEntidad(entidad.getAddress())
                .telefonoEntidad(Long.valueOf(entidad.getPhone()))
                .emailEntidad(entidad.getEmail())
                .estadoEntidad(entidad.getEstado())
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
        Optional<Entidad> entidadOptional = entidadRepository.findByNit(nid.toString());


        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();

            Entidad entidadAntigua = new Entidad();
            BeanUtils.copyProperties(entidad, entidadAntigua);

            entidad.setEstado(EstadoEntidad.ACTIVA);

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

            entidad.setEstado(EstadoEntidad.NO_ACTIVA);

            logCambioService.registrarActualizacion(nombreEntidad, entidadAntigua,entidadRepository.save(entidad));
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
        logCambioService.registrarConsulta(nombreEntidad);
        return entidadRepository.findByNameContainingIgnoreCase(nombre);
    }   

    public List<Pensionado> listarPensionadosPorEntidad(Long idEntidad) {
    Entidad entidad = entidadRepository.findById(idEntidad)
            .orElseThrow(() -> new RuntimeException("No se encontró la entidad con ID: " + idEntidad));

    // TODO: Método getPensionados() ya no existe en Entidad
    return new ArrayList<>();
    }

    public List<Trabajo> listarTrabajosPorEntidad(Long idEntidad) {
    Entidad entidad = entidadRepository.findById(idEntidad)
            .orElseThrow(() -> new RuntimeException("No se encontró la entidad con ID: " + idEntidad));

    // TODO: Método getTrabajos() ya no existe en Entidad
    return new ArrayList<>();
    }


}