package com.sandrojam.modulomontagem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Payload recebido do ERP quando uma nota fiscal e emitida para um pedido
 * que contem ao menos um item de montagem. Este e o gatilho de entrada que
 * cria uma OrdemMontagem com origem = ERP.
 */
public record PedidoVendaWebhookDTO(
        @NotBlank String pedidoVendaId,
        @NotBlank String chaveNotaFiscal,
        @NotNull @Valid EmpresaWebhookDTO empresa,
        @NotNull @Valid FilialWebhookDTO filial,
        @NotNull @Valid ClienteWebhookDTO cliente,
        @NotBlank String enderecoEntrega,
        @NotEmpty @Valid List<ItemMontagemDTO> itensMontagem,
        LocalDate prazoCombinado,
        String observacoes
) {
    public record ClienteWebhookDTO(
            @NotBlank String idExterno,
            @NotBlank String nome,
            String telefone
    ) {
    }

    /** Empresa (matriz/razao social do grupo) que emitiu a nota fiscal. */
    public record EmpresaWebhookDTO(
            @NotBlank String idExterno,
            @NotBlank String nome
    ) {
    }

    /** Filial/loja especifica, dentro da empresa, que emitiu a nota fiscal. */
    public record FilialWebhookDTO(
            @NotBlank String idExterno,
            @NotBlank String nome
    ) {
    }
}
