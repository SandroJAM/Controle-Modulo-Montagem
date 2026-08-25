package com.sandrojam.modulomontagem.dto;

public record LoginResponseDTO(
        String token,
        String nome,
        String perfil
) {
}
