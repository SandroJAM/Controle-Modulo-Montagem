package com.sandrojam.modulomontagem.controller;

import com.sandrojam.modulomontagem.dto.MontadorDTO;
import com.sandrojam.modulomontagem.service.MontadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/montadores")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
public class MontadorController {

    private final MontadorService montadorService;

    @PostMapping
    public MontadorDTO criar(@Valid @RequestBody MontadorDTO dto) {
        return montadorService.criar(dto);
    }

    @GetMapping
    public List<MontadorDTO> listarAtivos() {
        return montadorService.listarAtivos();
    }

    @PatchMapping("/{id}")
    public MontadorDTO atualizar(@PathVariable Long id, @Valid @RequestBody MontadorDTO dto) {
        return montadorService.atualizar(id, dto);
    }
}
