package com.sandrojam.modulomontagem.dto;

import com.sandrojam.modulomontagem.model.OrigemOrdemMontagem;
import com.sandrojam.modulomontagem.model.StatusOrdemMontagem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrdemMontagemDTO(
        Long id,
        OrigemOrdemMontagem origem,
        StatusOrdemMontagem status,
        String pedidoVendaIdExterno,
        String chaveNotaFiscal,
        String empresaIdExterno,
        String empresaNome,
        String filialIdExterno,
        String filialNome,
        String clienteIdExterno,
        String clienteNome,
        String clienteTelefone,
        String enderecoEntrega,
        LocalDate prazoCombinado,
        String observacoes,
        BigDecimal valorServicoAvulso,
        List<ItemMontagemDTO> itens,
        LocalDateTime criadaEm,
        LocalDateTime atualizadaEm
) {
}
