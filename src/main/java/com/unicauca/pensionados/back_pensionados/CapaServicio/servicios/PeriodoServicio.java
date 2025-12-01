package com.unicauca.pensionados.back_pensionados.CapaServicio.servicios;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.CuotaParte;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.IPC;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Pensionado;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.modelos.Periodo;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.CuotaParteRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.IPCRepositorio;
import com.unicauca.pensionados.back_pensionados.capaAccesoADatos.repositories.PeriodoRepositorio;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PeriodoServicio implements IPeriodoServicio {

    @Autowired
    private PeriodoRepositorio periodoRepositorio;

    @Autowired
    private IPCRepositorio ipcRepositorio;

    @Autowired
    private CuotaParteRepositorio cuotaParteRepositorio;

    @Override
    @Transactional
    @Operation(
            summary = "Genera y calcula los periodos de pensión para una cuota parte específica",
            description = "Crea periodos anuales desde la fecha de inicio de pensión hasta la fecha actual, calculando el valor de la pensión, cuota parte mensual y total anual, considerando IPC y reajustes legales."
    )
    public void generarYCalcularPeriodos(LocalDate fechaInicioPension, CuotaParte cuotaParte) {
        List<Periodo> periodos = new ArrayList<>();
        LocalDate fechaActual = LocalDate.now();
        int anioInicio = fechaInicioPension.getYear();
        int anioActual = fechaActual.getYear();

        Pensionado pensionado = cuotaParte.getTrabajo().getPensionado();
        BigDecimal valorPensionAnterior = pensionado.getValorInicialPension();
        boolean aplicarIPCPrimerPeriodo = pensionado.isAplicarIPCPrimerPeriodo();
        BigDecimal porcentajeCuotaParte = cuotaParte.getPorcentajeCuotaParte(); 

        // Obtener los IPC desde el año de inicio
        List<IPC> IPCApartirFechaPension = ipcRepositorio.findByFechaIPCGreaterThanEqual(anioInicio - 1);
        Map<Integer, IPC> ipcPorAnio = IPCApartirFechaPension.stream()
            .collect(Collectors.toMap(IPC::getFechaIPC, Function.identity()));

        for (int anio = anioInicio; anio <= anioActual; anio++) {
            LocalDate inicioPeriodo;
            LocalDate finPeriodo;

            if (anio == anioInicio) {
                inicioPeriodo = fechaInicioPension;
                finPeriodo = LocalDate.of(anio, 12, 31);
            } else if (anio == anioActual) {
                inicioPeriodo = LocalDate.of(anio, 1, 1);
                finPeriodo = fechaActual;
            } else {
                inicioPeriodo = LocalDate.of(anio, 1, 1);
                finPeriodo = LocalDate.of(anio, 12, 31);
            }

            BigDecimal numeroMesadas = calcularMesadas(inicioPeriodo, finPeriodo);

            // Obtener IPC del año anterior 
            IPC ipc = ipcPorAnio.get(anio-1);
            BigDecimal valorIPC = ipc != null ? ipc.getValorIPC()
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

            // Calcular valor de pension actual
            BigDecimal valorPension;

            if (anio == anioInicio && aplicarIPCPrimerPeriodo) {
                valorPension = valorPensionAnterior.add(valorPensionAnterior.multiply(valorIPC));
            } else if (anio != anioInicio) {
                valorPension = valorPensionAnterior.add(valorPensionAnterior.multiply(valorIPC));
            } else {
                valorPension = valorPensionAnterior;
            }


            if(anio >= 1993 && anio <= 1997 && fechaInicioPension.getMonth().getValue() <= 7){
                valorPension = reajuste1993_1997(anio, pensionado, valorPension);
            }


            // Calcular cuota parte mensual y anual
            BigDecimal cuotaParteMensual = valorPension.multiply(porcentajeCuotaParte);
            BigDecimal cuotaParteTotalAnio = cuotaParteMensual.multiply(numeroMesadas);

            // Crear y agregar el periodo
            Periodo periodo = new Periodo();
            periodo.setFechaInicioPeriodo(inicioPeriodo);
            periodo.setFechaFinPeriodo(finPeriodo);
            periodo.setNumeroMesadas(numeroMesadas);
            periodo.setValorPension(valorPension);
            periodo.setCuotaParteMensual(cuotaParteMensual);
            periodo.setCuotaParteTotalAnio(cuotaParteTotalAnio);
            periodo.setIncrementoLey476(ipc.getValorIPC()); 
            periodo.setIPC(ipc);
            periodo.setCuotaParte(cuotaParte);
            periodos.add(periodo);
            valorPensionAnterior = valorPension;
        }

        periodoRepositorio.saveAll(periodos);

        BigDecimal sumaPeriodos = periodos.stream()
        .map(Periodo::getCuotaParteTotalAnio)
        .filter(Objects::nonNull)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
        cuotaParte.setValorTotalCuotaParte(sumaPeriodos);
        cuotaParteRepositorio.save(cuotaParte);

    }

    @Operation(
            summary = "Calcula el número total de mesadas pensionales entre dos fechas",
            description = "Incluye mesadas ordinarias y adicionales (junio y diciembre) según la cobertura anual entre las fechas de inicio y fin."
    )
    public BigDecimal calcularMesadas(LocalDate inicio, LocalDate fin) {
        BigDecimal totalMesadas = BigDecimal.ZERO;

        int anioInicio = inicio.getYear();
        int anioFin = fin.getYear();

        for (int anio = anioInicio; anio <= anioFin; anio++) {
            LocalDate inicioAnio = LocalDate.of(anio, 1, 1);
            LocalDate finAnio = LocalDate.of(anio, 12, 31);

            LocalDate desde = (inicio.isAfter(inicioAnio)) ? inicio : inicioAnio;
            LocalDate hasta = (fin.isBefore(finAnio)) ? fin : finAnio;

            long diasPeriodo = ChronoUnit.DAYS.between(desde, hasta) + 1;
            long diasAnio = ChronoUnit.DAYS.between(inicioAnio, finAnio) + 1;

            if (diasPeriodo <= 0) continue;

            // Mesadas ordinarias
            BigDecimal mesadas = BigDecimal.valueOf(12)
                .multiply(BigDecimal.valueOf(diasPeriodo))
                .divide(BigDecimal.valueOf(diasAnio), 5, RoundingMode.HALF_UP);

            // Junio adicional
            LocalDate inicioJunio = LocalDate.of(anio, 6, 1);
            LocalDate finJunio = YearMonth.of(anio, 6).atEndOfMonth();
            if (anio >= 1994 && !hasta.isBefore(inicioJunio) && !desde.isAfter(finJunio)) {
                long diasEnJunio = ChronoUnit.DAYS.between(inicioJunio, finJunio) + 1;
                long diasCubrimiento = ChronoUnit.DAYS.between(
                    desde.isAfter(inicioJunio) ? desde : inicioJunio,
                    hasta.isBefore(finJunio) ? hasta : finJunio
                ) + 1;
                if (diasCubrimiento > 0) {
                    BigDecimal proporcion = BigDecimal.valueOf(diasCubrimiento)
                        .divide(BigDecimal.valueOf(diasEnJunio), 5, RoundingMode.HALF_UP);
                    mesadas = mesadas.add(proporcion);
                }
            }

            // Diciembre adicional
            LocalDate inicioDic = LocalDate.of(anio, 12, 1);
            LocalDate finDic = YearMonth.of(anio, 12).atEndOfMonth();
            if (!hasta.isBefore(inicioDic) && !desde.isAfter(finDic)) {
                long diasEnDic = ChronoUnit.DAYS.between(inicioDic, finDic) + 1;
                long diasCubrimiento = ChronoUnit.DAYS.between(
                    desde.isAfter(inicioDic) ? desde : inicioDic,
                    hasta.isBefore(finDic) ? hasta : finDic
                ) + 1;
                if (diasCubrimiento > 0) {
                    BigDecimal proporcion = BigDecimal.valueOf(diasCubrimiento)
                        .divide(BigDecimal.valueOf(diasEnDic), 5, RoundingMode.HALF_UP);
                    mesadas = mesadas.add(proporcion);
                }
            }

            totalMesadas = totalMesadas.add(mesadas);
        }
        return totalMesadas.setScale(2, RoundingMode.HALF_UP);
    }

    @Operation(
            summary = "Aplica los reajustes legales a la pensión entre 1993 y 1997",
            description = "Calcula el valor reajustado de la pensión según el año de inicio de pensión y el año a calcular, aplicando los porcentajes establecidos por la ley."
    )
    public BigDecimal reajuste1993_1997(int anio, Pensionado pensionado, BigDecimal valorPension) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(java.sql.Date.valueOf(pensionado.getFechaInicioPension()));

        int anioInicioPension = cal.get(Calendar.YEAR);
        if (anio == 1993 || anio == 1994) {
            if (anioInicioPension <= 1981 ) {
                valorPension = valorPension.multiply(new BigDecimal("0.12")).add(valorPension);
            }else if(anioInicioPension > 1981 && anioInicioPension <= 1988){
                valorPension = valorPension.multiply(new BigDecimal("0.07")).add(valorPension);
            }
            if(anio == 1994){
                valorPension = valorPension.multiply(new BigDecimal("0.03025")).add(valorPension);
            }
            return valorPension;
        }

        if (anio == 1995){
            if(anioInicioPension <= 1981){
                valorPension = valorPension.multiply(new BigDecimal("0.04")).add(valorPension);
            }
            valorPension = valorPension.multiply(new BigDecimal("0.02")).add(valorPension);
            return valorPension;
        }


        if (anio == 1996 || anio == 1997){
            valorPension = valorPension.multiply(new BigDecimal("0.01")).add(valorPension);
            return valorPension;
        }

        return valorPension;
    }

}
