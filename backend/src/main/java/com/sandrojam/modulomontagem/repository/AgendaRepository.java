package com.sandrojam.modulomontagem.repository;

import com.sandrojam.modulomontagem.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByMontadorIdAndDataHoraBetween(Long montadorId, java.time.LocalDateTime inicio, java.time.LocalDateTime fim);
    List<Agenda> findByOrdemMontagemId(Long ordemMontagemId);
}
