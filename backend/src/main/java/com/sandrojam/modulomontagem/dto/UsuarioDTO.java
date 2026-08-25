package com.sandrojam.modulomontagem.dto;

import com.sandrojam.modulomontagem.model.PerfilUsuarioMontagem;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDTO(
        Long id,
        @NotBlank String nome,
        @NotBlank @Email String email,
        String senha, // usado somente na criacao/alteracao de senha; nunca retornado
        @NotNull PerfilUsuarioMontagem perfil,
        boolean ativo
) {
}
