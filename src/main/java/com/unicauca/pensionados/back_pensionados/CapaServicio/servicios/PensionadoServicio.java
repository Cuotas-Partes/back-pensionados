package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;


import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Entidad;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Trabajo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.EntidadRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PensionadoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PersonaRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.TrabajoRepositorio;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.peticion.RegistroPensionadoPeticion;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.EntidadCuotaParteRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.PensionadoRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.SucesorRespuesta;
import com.unicauca.pensionados.back_pensionados.capaPresentacion.dto.respuesta.TrabajoRespuesta;

import jakarta.transaction.Transactional;
//import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class PensionadoServicio implements IPensionadoServicio {

    private final PersonaRepositorio personaRepositorio;
    private final PensionadoRepositorio pensionadoRepositorio;
    private final EntidadRepositorio entidadRepositorio;
    private final TrabajoRepositorio trabajoRepositorio;
    private final CuotaParteServicio cuotaParteServicio;
    private final CuotaParteRepositorio cuotaParteRepositorio;

    public PensionadoServicio(PersonaRepositorio personaRepositorio,
                               PensionadoRepositorio pensionadoRepositorio,
                               EntidadRepositorio entidadRepositorio,
                               TrabajoRepositorio trabajoRepositorio,
                               CuotaParteServicio cuotaParteServicio, 
                               CuotaParteRepositorio cuotaParteRepositorio) {
        this.personaRepositorio = personaRepositorio;
        this.pensionadoRepositorio = pensionadoRepositorio;
        this.entidadRepositorio = entidadRepositorio;
        this.trabajoRepositorio = trabajoRepositorio;
        this.cuotaParteServicio = cuotaParteServicio;
        this.cuotaParteRepositorio = cuotaParteRepositorio;
    }
    @Transactional
    @Override
    public void registrarPensionado(RegistroPensionadoPeticion request) {

        if (personaRepositorio.existsById(request.getNumeroIdPersona())) {
            throw new RuntimeException("El número de identificación ya se encuentra registrado");
        }

        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubiló no se encuentra registrada"));

        Pensionado pensionado = new Pensionado();
        pensionado.setNumeroIdPersona(request.getNumeroIdPersona());
        pensionado.setTipoIdPersona(request.getTipoIdPersona());
        pensionado.setNombrePersona(request.getNombrePersona());
        pensionado.setApellidosPersona(request.getApellidosPersona());
        pensionado.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionado.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionado.setEstadoPersona(request.getEstadoPersona());
        pensionado.setGeneroPersona(request.getGeneroPersona());
        pensionado.setFechaInicioPension(request.getFechaInicioPension());
        pensionado.setValorInicialPension(request.getValorInicialPension());
        pensionado.setResolucionPension(request.getResolucionPension());
        pensionado.setEntidadJubilacion(entidad);
        pensionado.setTotalDiasTrabajo(request.getDiasDeServicio());
        pensionado.setAplicarIPCPrimerPeriodo(request.isAplicarIPCPrimerPeriodo());
        //pensionado.setFechaDefuncionPersona(request.getFechaDefuncionPersona());

        Trabajo trabajo = new Trabajo();
        trabajo.setDiasDeServicio(request.getDiasDeServicio());
        trabajo.setEntidad(entidad); 
        trabajo.setPensionado(pensionado); 

    
        pensionado.setTotalDiasTrabajo(request.getDiasDeServicio());
        pensionadoRepositorio.save(pensionado);
        trabajoRepositorio.save(trabajo);
        cuotaParteServicio.registrarCuotaParte(trabajo);
        /*//Generamos automaticamente la cuota parte que va a estar relacionada a un trabajo

        CuotaParte cuotaParte = new CuotaParte();
        MonetaryAmount valorInicialPension = Money.of(request.getValorInicialPension(), Monetary.getCurrency("COP"));
        BigDecimal porcentajeCuotaParte = BigDecimal.valueOf(request.getDiasDeServicio()/pensionado.getTotalDiasTrabajo());
        MonetaryAmount valorCuotaParteMoney=Money.of(0, Monetary.getCurrency("COP"));
        valorCuotaParteMoney = valorInicialPension.multiply(porcentajeCuotaParte);

        cuotaParte.setTrabajo(trabajo);
        cuotaParte.setValorCuotaParte(valorCuotaParteMoney.getNumber().numberValue(BigDecimal.class));
        cuotaParte.setPorcentajeCuotaParte(porcentajeCuotaParte);
        cuotaParte.setFechaGeneracion(LocalDate.now());
        cuotaParte.setNotas("Si funiono la parte de registro de cuota Parte");

        cuotaParteRepositorio.save(cuotaParte);*/
    }

    /**
     * Actualizar pensionado en la base de datos.
     * @param nid el NIT de la entidad a actualizar
     * @param pensionado los nuevos datos de la entidad
     * @return mensaje de éxito
     * @throws RuntimeException si no se encuentra la entidad
     * @throws Exception si ocurre un error al actualizar la entidad
     */
    @Transactional
    @Override
    public void actualizarPensionado(Long numeroIdPersona, RegistroPensionadoPeticion request) {
        Pensionado pensionadoExistente = pensionadoRepositorio.findById(numeroIdPersona)
                .orElseThrow(() -> new RuntimeException("No se encontró la persona con este número de identidad: " + numeroIdPersona));

        Entidad entidad = entidadRepositorio.findById(request.getNitEntidad())
                .orElseThrow(() -> new RuntimeException("La Entidad en la que el pensionado se jubiló no se encuentra registrada"));

        pensionadoExistente.setNombrePersona(request.getNombrePersona());
        pensionadoExistente.setApellidosPersona(request.getApellidosPersona());
        pensionadoExistente.setFechaNacimientoPersona(request.getFechaNacimientoPersona());
        pensionadoExistente.setFechaExpedicionDocumentoIdPersona(request.getFechaExpedicionDocumentoIdPersona());
        pensionadoExistente.setEstadoPersona(request.getEstadoPersona());
        pensionadoExistente.setGeneroPersona(request.getGeneroPersona());
        pensionadoExistente.setFechaInicioPension(request.getFechaInicioPension());
        pensionadoExistente.setValorInicialPension(request.getValorInicialPension());
        pensionadoExistente.setResolucionPension(request.getResolucionPension());
        pensionadoExistente.setAplicarIPCPrimerPeriodo(request.isAplicarIPCPrimerPeriodo());
        pensionadoExistente.setFechaDefuncionPersona(request.getFechaDefuncionPersona());

        // Comparar correctamente los NIT (ambos son Long)
        if (!pensionadoExistente.getEntidadJubilacion().getNitEntidad().equals(request.getNitEntidad())) {
            // Eliminar trabajo y cuota parte anteriores si cambia la entidad de jubilación
            Optional<Entidad> entidadAnterior = entidadRepositorio.findById(pensionadoExistente.getEntidadJubilacion().getNitEntidad());
            entidadAnterior.ifPresent(entAnt -> {
                Optional<Trabajo> trabajoAnterior = trabajoRepositorio.findByPensionadoAndEntidad(pensionadoExistente, entAnt);
                trabajoAnterior.ifPresent(trabajoExistente -> {
                    Optional<CuotaParte> cuotaParteAnterior = cuotaParteRepositorio.findByTrabajoIdTrabajo(trabajoExistente.getIdTrabajo());
                    cuotaParteAnterior.ifPresent(cuotaParteRepositorio::delete);
                    trabajoRepositorio.delete(trabajoExistente);
                });
            });
        }

        pensionadoExistente.setEntidadJubilacion(entidad);
        pensionadoRepositorio.save(pensionadoExistente);

        // Buscar o crear el trabajo actual
        Optional<Trabajo> trabajoOptional = trabajoRepositorio.findByPensionadoAndEntidad(pensionadoExistente, entidad);
        Trabajo trabajo = trabajoOptional.orElseGet(() -> {
            Trabajo nuevoTrabajo = new Trabajo();
            nuevoTrabajo.setPensionado(pensionadoExistente);
            nuevoTrabajo.setEntidad(entidad);
            return nuevoTrabajo;
        });

        trabajo.setDiasDeServicio(request.getDiasDeServicio());
        trabajo.setEntidad(entidad);
        trabajoRepositorio.save(trabajo);

        // Actualizar total de días de trabajo
        Long totalDiasTrabajo = trabajoRepositorio.findByPensionado(pensionadoExistente)
                .stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();
        pensionadoExistente.setTotalDiasTrabajo(totalDiasTrabajo);
        pensionadoRepositorio.save(pensionadoExistente);

        cuotaParteServicio.registrarCuotaParte(trabajo);
    }

    @Override
    public List<PensionadoRespuesta> listarPensionados() {
        List<Pensionado> pensionados = pensionadoRepositorio.findAll();
        List<PensionadoRespuesta> respuestas = new ArrayList<>();
        for (Pensionado pensionado : pensionados) {
            // Mapeo de Sucesores
            List<SucesorRespuesta> sucesoresRespuesta = new ArrayList<>();
            if (pensionado.getSucesores() != null) {
                sucesoresRespuesta = pensionado.getSucesores().stream()
                    .map(sucesor -> SucesorRespuesta.builder()
                        .numeroIdPersona(sucesor.getNumeroIdPersona())
                        .tipoIdPersona(sucesor.getTipoIdPersona())
                        .nombrePersona(sucesor.getNombrePersona())
                        .apellidosPersona(sucesor.getApellidosPersona())
                        .fechaNacimientoPersona(sucesor.getFechaNacimientoPersona())
                        .fechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona())
                        .estadoPersona(sucesor.getEstadoPersona())
                        .generoPersona(sucesor.getGeneroPersona())
                        .fechaDefuncionPersona(sucesor.getFechaDefuncionPersona())
                        .fechaInicioSucesion(sucesor.getFechaInicioSucesion())
                        .porcentajePension(sucesor.getPorcentajePension())
                        .build())
                    .collect(Collectors.toList());
            }

            respuestas.add(PensionadoRespuesta.builder()
                .numeroIdPersona(pensionado.getNumeroIdPersona())
                .tipoIdPersona(pensionado.getTipoIdPersona())
                .nombrePersona(pensionado.getNombrePersona())
                .apellidosPersona(pensionado.getApellidosPersona())
                .fechaNacimientoPersona(pensionado.getFechaNacimientoPersona())
                .fechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona())
                .estadoPersona(pensionado.getEstadoPersona())
                .generoPersona(pensionado.getGeneroPersona())
                .fechaInicioPension(pensionado.getFechaInicioPension())
                .valorInicialPension(pensionado.getValorInicialPension())
                .resolucionPension(pensionado.getResolucionPension())
                .nitEntidad(pensionado.getEntidadJubilacion().getNitEntidad())
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(pensionado.getTotalDiasTrabajo())
                .fechaDefuncionPersona(pensionado.getFechaDefuncionPersona())
                .aplicarIPCPrimerPeriodo(pensionado.isAplicarIPCPrimerPeriodo())
                .diasDeServicio(pensionado.getTrabajos().stream()
                        .filter(trabajo -> trabajo.getEntidad().getNitEntidad()
                                .equals(pensionado.getEntidadJubilacion().getNitEntidad()))
                        .mapToLong(Trabajo::getDiasDeServicio)
                        .findFirst()
                        .orElse(0L))
                .trabajos(pensionado.getTrabajos().stream()
                        .map(trabajo -> TrabajoRespuesta.builder()
                                .idTrabajo(trabajo.getIdTrabajo())
                                .diasDeServicio(trabajo.getDiasDeServicio())
                                .nitEntidad(trabajo.getEntidad().getNitEntidad())
                                .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                                .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                                .build())
                        .collect(Collectors.toList()))
                .sucesores(sucesoresRespuesta)
                .aplicarIPCPrimerPeriodo(pensionado.isAplicarIPCPrimerPeriodo())
                .build());
        }
        return respuestas;
    }




    /**
     * Busca pensionados por su nombre.
     * 
     * @param nombre el nombre del pensionado a buscar
     * @return una lista de objetos Pensionado
     */
    @Override
    public List<Pensionado> buscarPensionadosPorNombre(String nombre) {
        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCase(nombre);
    }

    /**
     * Busca pensionados por su apellido.
     * 
     * @param apellido el apellido del pensionado a buscar
     * @return una lista de objetos Pensionado
     */
    @Override
    public List<Pensionado> buscarPensionadosPorApellido(String apellido) {
        return pensionadoRepositorio.findByApellidosPersonaContainingIgnoreCase(apellido);
    }

    /**
     * Busca pensionados por un criterio de búsqueda.
     * 
     * @param query el criterio de búsqueda
     * @return una lista de objetos Pensionado
     */
    @Override
    public List<Pensionado> buscarPensionadosPorCriterio(String query) {
        if (query == null || query.trim().isEmpty()) {
            return pensionadoRepositorio.findAll();
        }
        
        query = query.trim();
        
        if (query.matches("\\d+")) {
            Long id = Long.parseLong(query);
            return pensionadoRepositorio.findById(id)
                    .map(List::of)
                    .orElseGet(ArrayList::new);
        }

        // Búsqueda por nombre o apellido
        return pensionadoRepositorio.findByNombrePersonaContainingIgnoreCaseOrApellidosPersonaContainingIgnoreCase(query, query);
    }

    /**
     * Desactiva un pensionado por su ID.
     * 
     * @param id el ID del pensionado a desactivar
     * @throws RuntimeException si no se encuentra el pensionado
     */
    @Transactional
    @Override
    public void desactivarPensionado(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Pensionado no encontrado con ID: " + id));
        
        pensionado.setEstadoPersona("Inactivo");
        pensionadoRepositorio.save(pensionado);
    }


    @Override
    public PensionadoRespuesta buscarPensionadoPorId(Long id) {
        Pensionado pensionado = pensionadoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el pensionado con ID: " + id));

        Long totalDiasTrabajo = pensionado.getTrabajos().stream()
                .mapToLong(Trabajo::getDiasDeServicio)
                .sum();

        Long diasDeServicioJubilacion = pensionado.getTrabajos().stream()
            .filter(trabajo -> trabajo.getEntidad().getNitEntidad()
                    .equals(pensionado.getEntidadJubilacion().getNitEntidad()))
            .mapToLong(Trabajo::getDiasDeServicio)
            .findFirst()
            .orElse(0L);

        List<TrabajoRespuesta> trabajos = pensionado.getTrabajos().stream()
                .map(trabajo -> TrabajoRespuesta.builder()
                        .idTrabajo(trabajo.getIdTrabajo())
                        .diasDeServicio(trabajo.getDiasDeServicio())
                        .nitEntidad(trabajo.getEntidad().getNitEntidad())
                        .numeroIdPersona(trabajo.getPensionado().getNumeroIdPersona())
                        .entidadJubilacion(trabajo.getEntidad().getNombreEntidad())
                        .build())
                .toList();
        
        // Mapeo de Sucesores
        List<SucesorRespuesta> sucesoresRespuesta = new ArrayList<>();
        if (pensionado.getSucesores() != null) {
            sucesoresRespuesta = pensionado.getSucesores().stream()
                .map(sucesor -> SucesorRespuesta.builder()
                    .numeroIdPersona(sucesor.getNumeroIdPersona())
                    .tipoIdPersona(sucesor.getTipoIdPersona())
                    .nombrePersona(sucesor.getNombrePersona())
                    .apellidosPersona(sucesor.getApellidosPersona())
                    .fechaNacimientoPersona(sucesor.getFechaNacimientoPersona())
                    .fechaExpedicionDocumentoIdPersona(sucesor.getFechaExpedicionDocumentoIdPersona())
                    .estadoPersona(sucesor.getEstadoPersona())
                    .generoPersona(sucesor.getGeneroPersona())
                    .fechaDefuncionPersona(sucesor.getFechaDefuncionPersona())
                    .fechaInicioSucesion(sucesor.getFechaInicioSucesion())
                    .porcentajePension(sucesor.getPorcentajePension())
                    .build())
                .collect(Collectors.toList());
        }

        return PensionadoRespuesta.builder()
                .numeroIdPersona(pensionado.getNumeroIdPersona())
                .tipoIdPersona(pensionado.getTipoIdPersona())
                .nombrePersona(pensionado.getNombrePersona())
                .apellidosPersona(pensionado.getApellidosPersona())
                .fechaNacimientoPersona(pensionado.getFechaNacimientoPersona())
                .fechaExpedicionDocumentoIdPersona(pensionado.getFechaExpedicionDocumentoIdPersona())
                .estadoPersona(pensionado.getEstadoPersona())
                .generoPersona(pensionado.getGeneroPersona())           
                .fechaInicioPension(pensionado.getFechaInicioPension())
                .valorInicialPension(pensionado.getValorInicialPension())
                .resolucionPension(pensionado.getResolucionPension())
                .nitEntidad(pensionado.getEntidadJubilacion().getNitEntidad())
                .entidadJubilacion(pensionado.getEntidadJubilacion().getNombreEntidad())
                .totalDiasTrabajo(totalDiasTrabajo)
                .fechaDefuncionPersona(pensionado.getFechaDefuncionPersona())
                .aplicarIPCPrimerPeriodo(pensionado.isAplicarIPCPrimerPeriodo())
                .diasDeServicio(diasDeServicioJubilacion)
                .trabajos(trabajos)
                .sucesores(sucesoresRespuesta)
                .build();
    }

    @Override
    public List<EntidadCuotaParteRespuesta> getEntidadesYCuotaParteByPensionadoId(Long pensionadoId) {
        return pensionadoRepositorio.findEntidadesYCuotaParteByPensionadoId(pensionadoId);
    }

}