package com.sandrojam.modulomontagem.dto;

import jakarta.validation.constraints.NotBlank;

public record OcorrenciaDTO(
        Long id,
        @NotBlank String tipo,
        @NotBlank String descricao
) {
}
