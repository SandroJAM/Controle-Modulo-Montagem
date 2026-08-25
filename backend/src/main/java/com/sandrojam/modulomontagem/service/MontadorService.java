package com.sandrojam.modulomontagem.service;

import com.sandrojam.modulomontagem.dto.MontadorDTO;
import com.sandrojam.modulomontagem.exception.ResourceNotFoundException;
import com.sandrojam.modulomontagem.model.Montador;
import com.sandrojam.modulomontagem.repository.MontadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MontadorService {

    private final MontadorRepository montadorRepository;

    @Transactional
    public MontadorDTO criar(MontadorDTO dto) {
        Montador montador = Montador.builder()
                .nome(dto.nome())
                .telefone(dto.telefone())
                .areaAtuacao(dto.areaAtuacao())
                .terceirizado(dto.terceirizado())
                .ativo(true)
                .build();

        return toDTO(montadorRepository.save(montador));
    }

    public List<MontadorDTO> listarAtivos() {
        return montadorRepository.findByAtivoTrue().stream().map(this::toDTO).toList();
    }

    @Transactional
    public MontadorDTO atualizar(Long id, MontadorDTO dto) {
        Montador montador = montadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Montador nao encontrado: " + id));

        montador.setNome(dto.nome());
        montador.setTelefone(dto.telefone());
        montador.setAreaAtuacao(dto.areaAtuacao());
        montador.setTerceirizado(dto.terceirizado());
        montador.setAtivo(dto.ativo());

        return toDTO(montadorRepository.save(montador));
    }

    private MontadorDTO toDTO(Montador m) {
        return new MontadorDTO(m.getId(), m.getNome(), m.getTelefone(), m.getAreaAtuacao(), m.isTerceirizado(), m.isAtivo());
    }
}
