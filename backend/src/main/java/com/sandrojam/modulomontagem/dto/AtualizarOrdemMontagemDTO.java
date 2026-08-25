package com.sandrojam.modulomontagem.dto;

import com.sandrojam.modulomontagem.model.StatusOrdemMontagem;

import java.time.LocalDate;

/**
 * Campos permitidos para atualizacao parcial (PATCH) de uma Ordem de
 * Montagem: reagendamento de prazo, mudanca de status, observacoes.
 * Todos os campos sao opcionais -- so os informados (nao nulos) sao
 * aplicados.
 */
public record AtualizarOrdemMontagemDTO(
        StatusOrdemMontagem status,
        LocalDate prazoCombinado,
        String observacoes
) {
}
