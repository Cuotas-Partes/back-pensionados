package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import com.unicauca.pensionados.backend.domain.model.entity.IPC;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IPCRepositorio extends JpaRepository<IPC, Integer> {
	List<IPC> findByFechaIPCGreaterThanEqual(Integer year);
	Optional<IPC> findByFechaIPC(Integer fechaIPC);
}