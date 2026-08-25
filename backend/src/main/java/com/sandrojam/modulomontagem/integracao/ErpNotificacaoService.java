package com.sandrojam.modulomontagem.integracao;

import com.sandrojam.modulomontagem.model.OrdemMontagem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Responsavel por notificar o ERP sempre que o status de uma Ordem de
 * Montagem mudar. O ERP usa esses eventos apenas para acompanhamento (ex:
 * mostrar status para o atendimento) -- este modulo nunca escreve
 * diretamente em dados comerciais/financeiros do ERP.
 *
 * Implementacao inicial simples e sincrona. Se o volume de eventos crescer,
 * considerar mover para uma fila (RabbitMQ/SQS) para nao acoplar a resposta
 * da API a disponibilidade do ERP.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ErpNotificacaoService {

    private final RestClient restClient = RestClient.create();

    @Value("${app.erp.notificacao-url}")
    private String notificacaoUrl;

    @Value("${app.erp.webhook-api-key}")
    private String apiKey;

    public void notificarMudancaStatus(OrdemMontagem ordem) {
        // Ordens avulsas nao tem pedido de venda associado; nao ha o que
        // notificar ao ERP nesse caso.
        if (ordem.getPedidoVendaIdExterno() == null) {
            return;
        }

        try {
            restClient.post()
                    .uri(notificacaoUrl)
                    .header("X-Api-Key", apiKey)
                    .body(Map.of(
                            "pedidoVendaId", ordem.getPedidoVendaIdExterno(),
                            "ordemMontagemId", ordem.getId(),
                            "status", ordem.getStatus().name(),
                            "atualizadoEm", LocalDateTime.now().toString()
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            // Nao interrompe o fluxo do modulo se o ERP estiver indisponivel;
            // apenas registra para investigacao/reprocessamento posterior.
            log.warn("Falha ao notificar ERP sobre a ordem {}: {}", ordem.getId(), e.getMessage());
        }
    }
}
