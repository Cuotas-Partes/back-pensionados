package com.unicauca.pensionados.backend.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.unicauca.pensionados.backend.domain.model.entity.Usuario;


public interface UsuarioRepositorio extends JpaRepository<Usuario,Integer>{
    Optional<Usuario> findByUsername(String username);
} 
