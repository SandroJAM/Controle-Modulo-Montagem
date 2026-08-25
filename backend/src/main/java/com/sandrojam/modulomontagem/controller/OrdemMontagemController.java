package com.sandrojam.modulomontagem.controller;

import com.sandrojam.modulomontagem.dto.*;
import com.sandrojam.modulomontagem.service.OrdemMontagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordens-montagem")
@RequiredArgsConstructor
public class OrdemMontagemController {

    private final OrdemMontagemService ordemMontagemService;

    @PostMapping("/avulsa")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'ATENDENTE')")
    public ResponseEntity<OrdemMontagemDTO> criarAvulsa(@Valid @RequestBody OrdemMontagemAvulsaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemMontagemService.criarAvulsa(dto));
    }

    @GetMapping("/{id}")
    public OrdemMontagemDTO buscarPorId(@PathVariable Long id) {
        return ordemMontagemService.buscarPorId(id);
    }

    @GetMapping
    public List<OrdemMontagemDTO> listarOuBuscarPorPedido(
            @RequestParam(required = false) String pedidoId,
            @RequestParam(required = false) String empresaId,
            @RequestParam(required = false) String filialId) {

        if (pedidoId != null) {
            return List.of(ordemMontagemService.buscarPorPedidoVenda(pedidoId));
        }
        if (empresaId != null && filialId != null) {
            return ordemMontagemService.listarPorEmpresaEFilial(empresaId, filialId);
        }
        return ordemMontagemService.listar();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public OrdemMontagemDTO atualizar(@PathVariable Long id, @RequestBody AtualizarOrdemMontagemDTO dto) {
        return ordemMontagemService.atualizar(id, dto);
    }

    @PostMapping("/{id}/checklist")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'MONTADOR')")
    public ResponseEntity<Void> registrarChecklist(@PathVariable Long id, @RequestBody ChecklistDTO dto) {
        ordemMontagemService.registrarChecklist(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/ocorrencias")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'MONTADOR')")
    public ResponseEntity<OcorrenciaDTO> registrarOcorrencia(@PathVariable Long id, @Valid @RequestBody OcorrenciaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordemMontagemService.registrarOcorrencia(id, dto));
    }
}
