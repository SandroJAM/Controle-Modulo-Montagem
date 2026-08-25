package com.sandrojam.modulomontagem.controller;

import com.sandrojam.modulomontagem.dto.VolumePorFilialDTO;
import com.sandrojam.modulomontagem.model.OrigemOrdemMontagem;
import com.sandrojam.modulomontagem.service.RelatorioService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
public class RelatorioController {

    private final RelatorioService relatorioService;

    /**
     * GET /api/relatorios/volume-montagens-por-filial
     *     ?dataInicio=2026-08-01&dataFim=2026-08-31&origem=ERP
     *
     * Todos os parametros sao opcionais: sem eles, considera os ultimos
     * 30 dias e origem = ERP.
     */
    @GetMapping("/volume-montagens-por-filial")
    public List<VolumePorFilialDTO> volumePorFilial(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) OrigemOrdemMontagem origem
    ) {
        return relatorioService.volumePorFilial(dataInicio, dataFim, origem);
    }
}
