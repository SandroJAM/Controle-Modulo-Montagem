package com.sandrojam.modulomontagem.dto;

import jakarta.validation.constraints.NotBlank;

public record MontadorDTO(
        Long id,
        @NotBlank String nome,
        String telefone,
        String areaAtuacao,
        boolean terceirizado,
        boolean ativo
) {
}
