package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.EstadoEntidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.enumeradores.TipoIdentificacion;
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
                // Se busca al pensionado por su número de identificación, no por el ID primario.
                Pensionado pensionado = pensionadoRepositorio.findByTipoIdentificacionAndNumeroIdentificacion(
                registroTrabajoPeticion.getTipoIdentificacion(), 
                registroTrabajoPeticion.getNumeroIdentificacion())


    .orElseThrow(() -> new RuntimeException(
        "El pensionado con identificación " + registroTrabajoPeticion.getTipoIdentificacion() + 
        " " + registroTrabajoPeticion.getNumeroIdentificacion() + " no está registrado"));

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
     * @param nid el NIT de la entidad a actualizar
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

    if (entidadRepository.existsByNombreEntidad(entidad.getNombreEntidad())
        && !entidadExistente.getNombreEntidad().equals(entidad.getNombreEntidad())) {
        throw new RuntimeException("Ya existe una entidad con el nombre: " + entidad.getNombreEntidad());
    }

    entidadExistente.setNombreEntidad(entidad.getNombreEntidad());
    entidadExistente.setDireccionEntidad(entidad.getDireccionEntidad());
    entidadExistente.setTelefonoEntidad(entidad.getTelefonoEntidad());
    entidadExistente.setEmailEntidad(entidad.getEmailEntidad());
    entidadExistente.setEstadoEntidad(entidad.getEstadoEntidad());
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
            Long numeroIdentificacion = trabajoPeticion.getNumeroIdentificacion();
            TipoIdentificacion tipoIdentificacion = trabajoPeticion.getTipoIdentificacion();
            Pensionado pensionado = pensionadoRepositorio.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion)
                .orElseThrow(() -> new RuntimeException(
                    "El pensionado con identificación " + tipoIdentificacion + " " + numeroIdentificacion + " no está registrado"));

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
            pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
            pensionadoRepositorio.save(pensionado);
            }
        }

        */
        entidadRepository.save(entidadExistente);
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
        Entidad entidad = entidadRepository.findByNitEntidad(nitEntidad)
        .orElseThrow(() -> new RuntimeException("No se encontró la entidad con NIT: " + nitEntidad));


        List<Trabajo> trabajosActuales = trabajoRepositorio.findByEntidadNitEntidad(nitEntidad);
        
        // ==================== CORRECCIÓN 1: Clave del Mapa ====================
        // La clave del mapa debe ser el ID primario único del pensionado.
        Map<Long, Trabajo> mapaTrabajosActuales = trabajosActuales.stream()
                .collect(Collectors.toMap(trabajo -> trabajo.getPensionado().getIdPersona(), trabajo -> trabajo));

        for (RegistroTrabajoPeticion trabajoPeticion : trabajosActualizados) {
            
            // ==================== CORRECCIÓN 2: Búsqueda del Pensionado ====================
            // Se busca al pensionado usando su tipo y número de identificación.
            Long numeroIdentificacion = trabajoPeticion.getNumeroIdentificacion();
            TipoIdentificacion tipoIdentificacion = trabajoPeticion.getTipoIdentificacion();
            Pensionado pensionado = pensionadoRepositorio.findByTipoIdentificacionAndNumeroIdentificacion(tipoIdentificacion, numeroIdentificacion)
                    .orElseThrow(() -> new RuntimeException(
                            "El pensionado con identificación " + tipoIdentificacion + " " + numeroIdentificacion + " no está registrado"));

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
                    entidad.getTrabajos().remove(trabajoExistente);

                    Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionado)
                            .stream()
                            .mapToLong(Trabajo::getDiasDeServicio)
                            .sum();
                    pensionado.setTotalDiasTrabajo(totalDiasTrabajo);
                    pensionadoRepositorio.save(pensionado);
                    
                    // ==================== CORRECCIÓN 3: Remover del Mapa ====================
                    mapaTrabajosActuales.remove(idPersona);

                    boolean tieneMasTrabajosEnEntidad = trabajoRepositorio.findByPensionadoAndEntidad(pensionado, entidad).isPresent();
                    if (!tieneMasTrabajosEnEntidad) {
                        // ==================== CORRECCIÓN 4: Lógica removeIf ====================
                        entidad.getTrabajos().removeIf(t -> t.getPensionado().getIdPersona().equals(idPersona));
                    }
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
                // ==================== CORRECCIÓN 6: Lógica removeIf ====================
                entidad.getTrabajos().removeIf(t -> t.getPensionado().getIdPersona().equals(idPersona));
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
                // ==================== CORRECCIÓN 7: Lógica removeIf ====================
                entidad.getTrabajos().removeIf(t -> t.getPensionado().getIdPersona().equals(pensionado.getIdPersona()));
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
            
            // ==================== CORRECCIÓN Bloque 1: Mapeo de Trabajos ====================
            List<TrabajoRespuesta> trabajos = entidad.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                    .diasDeServicio(trabajo.getDiasDeServicio())
                    .nitEntidad(trabajo.getEntidad().getNitEntidad())
                    // Se cambia 'numeroIdPersona' por 'idPersona' para coincidir con el DTO y la entidad.
                    .idPersona(trabajo.getPensionado().getIdPersona()) 
                    .idTrabajo(trabajo.getIdTrabajo())
                    .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                    .build())
                .toList();

            // ==================== CORRECCIÓN Bloque 2: Mapeo de Pensionados ====================
            List<PensionadoRespuesta> pensionados = entidad.getTrabajos().stream()
                .map(Trabajo::getPensionado)
                .filter(Objects::nonNull)
                .distinct()
                .map((Pensionado p) -> PensionadoRespuesta.builder()
                    // Se añade el nuevo ID primario.
                    .idPersona(p.getIdPersona())
                    // Se usa el getter para el número de identificación.
                    .numeroIdentificacion(p.getNumeroIdentificacion())
                    // Se convierte el enum a String.
                    .tipoIdentificacion(p.getTipoIdentificacion())
                    .nombrePersona(p.getNombrePersona())
                    .apellidosPersona(p.getApellidosPersona())
                    // Se añade el nuevo campo 'estadoCivil' y se convierte el enum a String.
                    .estadoCivil(p.getEstadoCivil().name())
                    .fechaNacimientoPersona(p.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(p.getFechaExpedicionDocumentoIdPersona())
                    // Se convierte el enum a String.
                    .estadoPersona(p.getEstadoPersona())
                    // Se convierte el enum a String, manejando el caso de que sea nulo.

                    .generoPersona(p.getGeneroPersona() != null ? p.getGeneroPersona() : null)

                    .fechaDefuncionPersona(p.getFechaDefuncionPersona())
                    .fechaInicioPension(p.getFechaInicioPension())
                    .valorInicialPension(p.getValorInicialPension())
                    .resolucionPension(p.getResolucionPension())
                    .entidadJubilacion(entidad.getNombreEntidad())
                    .totalDiasTrabajo(p.getTotalDiasTrabajo()) // Se obtiene el total del propio pensionado.
                    .diasDeServicio(trabajoRepositorio.findByPensionadoAndEntidad(p, entidad).map(Trabajo::getDiasDeServicio).orElse(0L))
                    .nitEntidad(entidad.getNitEntidad())
                    // Se usa el nuevo nombre de la lista de trabajos ('dependencias') y se corrige el mapeo interno.
                    .trabajos(p.getTrabajos().stream()
                        .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                            .idPersona(trabajo.getPensionado().getIdPersona())
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
        return entidadRepository.findByNitEntidad(nit)
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
            entidadRepository.findByNitEntidad(nit).ifPresent(entidades::add);

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
            // ==================== CORRECCIÓN Bloque 1: Mapeo de Trabajos ====================
            List<TrabajoRespuesta> trabajos = entidad.getTrabajos().stream()
                .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                    .idTrabajo(trabajo.getIdTrabajo())
                    .diasDeServicio(trabajo.getDiasDeServicio())
                    .nitEntidad(trabajo.getEntidad().getNitEntidad())
                    // Se usa el ID primario del pensionado.
                    .idPersona(trabajo.getPensionado().getIdPersona())
                    .entidadJubilacion(trabajo.getEntidad().getNombreEntidad()) // Se añade este campo que faltaba
                    .build())
                .toList();
            
            // ==================== CORRECCIÓN Bloque 2: Mapeo de Pensionados ====================
            List<PensionadoRespuesta> pensionados = entidad.getTrabajos().stream()
                .map(Trabajo::getPensionado) 
                .filter(Objects::nonNull)
                .distinct()
                .map((Pensionado p) -> PensionadoRespuesta.builder()
                    // Se usan todos los campos nuevos y correctos del DTO y la entidad.
                    .idPersona(p.getIdPersona())
                    .numeroIdentificacion(p.getNumeroIdentificacion())
                    .tipoIdentificacion(p.getTipoIdentificacion())
                    .nombrePersona(p.getNombrePersona())
                    .apellidosPersona(p.getApellidosPersona())
                    .estadoCivil(p.getEstadoCivil().name())
                    .fechaNacimientoPersona(p.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(p.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(p.getEstadoPersona())

                    .generoPersona(p.getGeneroPersona() != null ? p.getGeneroPersona() : null)

                    .fechaDefuncionPersona(p.getFechaDefuncionPersona())
                    .fechaInicioPension(p.getFechaInicioPension())
                    .valorInicialPension(p.getValorInicialPension())
                    .resolucionPension(p.getResolucionPension())
                    .entidadJubilacion(entidad.getNombreEntidad())
                    .nitEntidad(entidad.getNitEntidad())
                    .totalDiasTrabajo(p.getTotalDiasTrabajo())
                    .diasDeServicio(trabajoRepositorio.findByPensionadoAndEntidad(p, entidad).map(Trabajo::getDiasDeServicio).orElse(0L))
                    // Se corrige el mapeo interno de la lista de trabajos del pensionado.
                    .trabajos(p.getTrabajos().stream()
                        .map((Trabajo trabajo) -> TrabajoRespuesta.builder()
                            .nitEntidad(trabajo.getEntidad().getNitEntidad())
                            .idPersona(trabajo.getPensionado().getIdPersona())
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
        Optional<Entidad> entidadOptional = entidadRepository.findByNitEntidad(nid);


        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();
            entidad.setEstadoEntidad(EstadoEntidad.ACTIVA);
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
        Optional<Entidad> entidadOptional = entidadRepository.findByNitEntidad(nid);


        if (entidadOptional.isPresent()) {
            Entidad entidad = entidadOptional.get();

            entidad.setEstadoEntidad(EstadoEntidad.NO_ACTIVA);

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

    public List<Pensionado> listarPensionadosPorEntidad(Long idEntidad) {
    Entidad entidad = entidadRepository.findById(idEntidad)
            .orElseThrow(() -> new RuntimeException("No se encontró la entidad con ID: " + idEntidad));

    return entidad.getPensionados();
    }

    public List<Trabajo> listarTrabajosPorEntidad(Long idEntidad) {
    Entidad entidad = entidadRepository.findById(idEntidad)
            .orElseThrow(() -> new RuntimeException("No se encontró la entidad con ID: " + idEntidad));

    return entidad.getTrabajos();
    }


}