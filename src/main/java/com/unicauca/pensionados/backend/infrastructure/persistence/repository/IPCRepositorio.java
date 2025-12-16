package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.IPC;
import com.unicauca.pensionados.backend.domain.model.enums.EstadoIPC;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPCRepositorio extends JpaRepository<IPC, Long> {
	List<IPC> findByYearGreaterThanEqual(Integer year);
	Optional<IPC> findByYear(Integer year);
	@Query("SELECT MAX(i.year) FROM IPC i")
	Integer obtenerUltimoAnioRegistrado();
	List<IPC> findByEstado(EstadoIPC estado);
	Optional<IPC> findByIdAndEstado(Long id, EstadoIPC estado);

}