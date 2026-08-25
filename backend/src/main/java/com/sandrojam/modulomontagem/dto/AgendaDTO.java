package com.sandrojam.modulomontagem.dto;

import com.sandrojam.modulomontagem.model.StatusAgenda;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendaDTO(
        Long id,
        @NotNull Long ordemMontagemId,
        @NotNull Long montadorId,
        @NotNull LocalDateTime dataHora,
        StatusAgenda status,
        String motivoReagendamento
) {
}
