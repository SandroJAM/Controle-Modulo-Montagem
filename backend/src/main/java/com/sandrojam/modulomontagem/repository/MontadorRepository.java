package com.sandrojam.modulomontagem.repository;

import com.sandrojam.modulomontagem.model.Montador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MontadorRepository extends JpaRepository<Montador, Long> {
    List<Montador> findByAtivoTrue();
}
