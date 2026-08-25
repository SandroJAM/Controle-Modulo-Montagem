package com.sandrojam.modulomontagem.controller;

import com.sandrojam.modulomontagem.dto.OrdemMontagemDTO;
import com.sandrojam.modulomontagem.dto.PedidoVendaWebhookDTO;
import com.sandrojam.modulomontagem.service.OrdemMontagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Ponto de entrada chamado pelo ERP. Autenticado via header X-Api-Key
 * (ver ApiKeyAuthFilter), nao via JWT de usuario.
 */
@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final OrdemMontagemService ordemMontagemService;

    @PostMapping("/pedido-venda")
    public ResponseEntity<OrdemMontagemDTO> receberPedidoComMontagem(@Valid @RequestBody PedidoVendaWebhookDTO payload) {
        OrdemMontagemDTO criada = ordemMontagemService.criarAPartirDoErp(payload);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }
}
