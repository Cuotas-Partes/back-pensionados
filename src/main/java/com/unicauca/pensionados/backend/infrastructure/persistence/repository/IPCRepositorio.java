package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.IPC;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPCRepositorio extends JpaRepository<IPC, Integer> {
	List<IPC> findByFechaIPCGreaterThanEqual(Integer year);
	boolean findByFechaIPC(Integer fechaIPC);
	@Query("SELECT MAX(i.year) FROM IPC i")
	Integer obtenerUltimoAnioRegistrado();
	List<IPC> findByActivoTrue();
	Optional<IPC> findByIdAndActivoTrue(Long id);

}