package com.sandrojam.modulomontagem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Payload para criacao manual de uma Ordem de Montagem avulsa, sem vinculo
 * com pedido de venda/nota fiscal do ERP. Roda fora do financeiro por
 * enquanto -- nao gera cobranca nem NF a partir deste modulo.
 */
public record OrdemMontagemAvulsaDTO(
        @NotBlank String clienteNome,
        String clienteTelefone,
        @NotBlank String enderecoEntrega,
        @NotEmpty @Valid List<ItemMontagemDTO> itens,
        LocalDate prazoCombinado,
        String observacoes,
        BigDecimal valorServicoAvulso,
        // Opcionais: preencher se a filial que atendeu o avulso tambem
        // precisar entrar nos relatorios por empresa/filial.
        String empresaIdExterno,
        String empresaNome,
        String filialIdExterno,
        String filialNome
) {
}
