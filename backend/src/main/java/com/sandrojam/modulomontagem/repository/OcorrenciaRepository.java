package com.sandrojam.modulomontagem.repository;

import com.sandrojam.modulomontagem.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {
    List<Ocorrencia> findByOrdemMontagemId(Long ordemMontagemId);
}
