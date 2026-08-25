package com.sandrojam.modulomontagem.service;

import com.sandrojam.modulomontagem.dto.AgendaDTO;
import com.sandrojam.modulomontagem.exception.ResourceNotFoundException;
import com.sandrojam.modulomontagem.model.Agenda;
import com.sandrojam.modulomontagem.model.Montador;
import com.sandrojam.modulomontagem.model.OrdemMontagem;
import com.sandrojam.modulomontagem.model.StatusAgenda;
import com.sandrojam.modulomontagem.model.StatusOrdemMontagem;
import com.sandrojam.modulomontagem.integracao.ErpNotificacaoService;
import com.sandrojam.modulomontagem.repository.AgendaRepository;
import com.sandrojam.modulomontagem.repository.MontadorRepository;
import com.sandrojam.modulomontagem.repository.OrdemMontagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final OrdemMontagemRepository ordemMontagemRepository;
    private final MontadorRepository montadorRepository;
    private final ErpNotificacaoService erpNotificacaoService;

    @Transactional
    public AgendaDTO agendar(AgendaDTO dto) {
        OrdemMontagem ordem = ordemMontagemRepository.findById(dto.ordemMontagemId())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de montagem nao encontrada: " + dto.ordemMontagemId()));

        Montador montador = montadorRepository.findById(dto.montadorId())
                .orElseThrow(() -> new ResourceNotFoundException("Montador nao encontrado: " + dto.montadorId()));

        Agenda agenda = Agenda.builder()
                .ordemMontagem(ordem)
                .montador(montador)
                .dataHora(dto.dataHora())
                .status(StatusAgenda.AGENDADO)
                .build();

        Agenda salva = agendaRepository.save(agenda);

        ordem.setStatus(StatusOrdemMontagem.AGENDADA);
        OrdemMontagem ordemSalva = ordemMontagemRepository.save(ordem);
        erpNotificacaoService.notificarMudancaStatus(ordemSalva);

        return toDTO(salva);
    }

    public List<AgendaDTO> listarPorOrdem(Long ordemMontagemId) {
        return agendaRepository.findByOrdemMontagemId(ordemMontagemId).stream().map(this::toDTO).toList();
    }

    private AgendaDTO toDTO(Agenda a) {
        return new AgendaDTO(a.getId(), a.getOrdemMontagem().getId(), a.getMontador().getId(),
                a.getDataHora(), a.getStatus(), a.getMotivoReagendamento());
    }
}
