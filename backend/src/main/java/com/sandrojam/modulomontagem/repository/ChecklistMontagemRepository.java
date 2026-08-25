package com.sandrojam.modulomontagem.repository;

import com.sandrojam.modulomontagem.model.ChecklistMontagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChecklistMontagemRepository extends JpaRepository<ChecklistMontagem, Long> {
    Optional<ChecklistMontagem> findByOrdemMontagemId(Long ordemMontagemId);
}
