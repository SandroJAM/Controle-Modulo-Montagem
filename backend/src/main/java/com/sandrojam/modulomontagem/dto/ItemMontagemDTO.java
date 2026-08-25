package com.sandrojam.modulomontagem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemMontagemDTO(
        Long id,
        String produtoIdExterno,
        @NotBlank String descricao,
        @NotNull @Positive Integer quantidade
) {
}
