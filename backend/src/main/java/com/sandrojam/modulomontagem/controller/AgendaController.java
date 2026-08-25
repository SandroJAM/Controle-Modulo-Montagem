package com.sandrojam.modulomontagem.controller;

import com.sandrojam.modulomontagem.dto.AgendaDTO;
import com.sandrojam.modulomontagem.service.AgendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agenda")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public AgendaDTO agendar(@Valid @RequestBody AgendaDTO dto) {
        return agendaService.agendar(dto);
    }

    @GetMapping
    public List<AgendaDTO> listarPorOrdem(@RequestParam Long ordemMontagemId) {
        return agendaService.listarPorOrdem(ordemMontagemId);
    }
}
