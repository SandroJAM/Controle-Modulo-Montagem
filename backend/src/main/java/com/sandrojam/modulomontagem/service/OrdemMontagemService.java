package com.sandrojam.modulomontagem.service;

import com.sandrojam.modulomontagem.dto.*;
import com.sandrojam.modulomontagem.exception.RegraNegocioException;
import com.sandrojam.modulomontagem.exception.ResourceNotFoundException;
import com.sandrojam.modulomontagem.integracao.ErpNotificacaoService;
import com.sandrojam.modulomontagem.model.*;
import com.sandrojam.modulomontagem.repository.ChecklistMontagemRepository;
import com.sandrojam.modulomontagem.repository.OcorrenciaRepository;
import com.sandrojam.modulomontagem.repository.OrdemMontagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemMontagemService {

    private final OrdemMontagemRepository ordemMontagemRepository;
    private final ChecklistMontagemRepository checklistMontagemRepository;
    private final OcorrenciaRepository ocorrenciaRepository;
    private final ErpNotificacaoService erpNotificacaoService;

    // ---- Criacao via ERP (webhook de entrada) ----

    @Transactional
    public OrdemMontagemDTO criarAPartirDoErp(PedidoVendaWebhookDTO payload) {
        if (ordemMontagemRepository.existsByChaveNotaFiscal(payload.chaveNotaFiscal())) {
            throw new RegraNegocioException(
                    "Ja existe uma ordem de montagem para a nota fiscal " + payload.chaveNotaFiscal());
        }

        OrdemMontagem ordem = OrdemMontagem.builder()
                .origem(OrigemOrdemMontagem.ERP)
                .status(StatusOrdemMontagem.CRIADA)
                .pedidoVendaIdExterno(payload.pedidoVendaId())
                .chaveNotaFiscal(payload.chaveNotaFiscal())
                .empresaIdExterno(payload.empresa().idExterno())
                .empresaNome(payload.empresa().nome())
                .filialIdExterno(payload.filial().idExterno())
                .filialNome(payload.filial().nome())
                .clienteIdExterno(payload.cliente().idExterno())
                .clienteNome(payload.cliente().nome())
                .clienteTelefone(payload.cliente().telefone())
                .enderecoEntrega(payload.enderecoEntrega())
                .prazoCombinado(payload.prazoCombinado())
                .observacoes(payload.observacoes())
                .build();

        payload.itensMontagem().forEach(item -> ordem.getItens().add(
                ItemMontagem.builder()
                        .ordemMontagem(ordem)
                        .produtoIdExterno(item.produtoIdExterno())
                        .descricao(item.descricao())
                        .quantidade(item.quantidade())
                        .build()
        ));

        OrdemMontagem salva = ordemMontagemRepository.save(ordem);
        erpNotificacaoService.notificarMudancaStatus(salva); // OM.criada_confirmada
        return toDTO(salva);
    }

    // ---- Criacao avulsa (manual, fora do financeiro) ----

    @Transactional
    public OrdemMontagemDTO criarAvulsa(OrdemMontagemAvulsaDTO payload) {
        OrdemMontagem ordem = OrdemMontagem.builder()
                .origem(OrigemOrdemMontagem.AVULSA)
                .status(StatusOrdemMontagem.CRIADA)
                .clienteNome(payload.clienteNome())
                .clienteTelefone(payload.clienteTelefone())
                .enderecoEntrega(payload.enderecoEntrega())
                .prazoCombinado(payload.prazoCombinado())
                .observacoes(payload.observacoes())
                .valorServicoAvulso(payload.valorServicoAvulso())
                .empresaIdExterno(payload.empresaIdExterno())
                .empresaNome(payload.empresaNome())
                .filialIdExterno(payload.filialIdExterno())
                .filialNome(payload.filialNome())
                .build();

        payload.itens().forEach(item -> ordem.getItens().add(
                ItemMontagem.builder()
                        .ordemMontagem(ordem)
                        .produtoIdExterno(item.produtoIdExterno())
                        .descricao(item.descricao())
                        .quantidade(item.quantidade())
                        .build()
        ));

        return toDTO(ordemMontagemRepository.save(ordem));
    }

    // ---- Consultas ----

    public OrdemMontagemDTO buscarPorId(Long id) {
        return toDTO(buscarOuFalhar(id));
    }

    public OrdemMontagemDTO buscarPorPedidoVenda(String pedidoVendaId) {
        return ordemMontagemRepository.findByPedidoVendaIdExterno(pedidoVendaId)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Nenhuma ordem de montagem encontrada para o pedido " + pedidoVendaId));
    }

    public List<OrdemMontagemDTO> listar() {
        return ordemMontagemRepository.findAll().stream().map(this::toDTO).toList();
    }

    public List<OrdemMontagemDTO> listarPorEmpresaEFilial(String empresaId, String filialId) {
        return ordemMontagemRepository.findByEmpresaIdExternoAndFilialIdExterno(empresaId, filialId)
                .stream().map(this::toDTO).toList();
    }

    // ---- Atualizacao (reagendar prazo, mudar status, observacoes) ----

    @Transactional
    public OrdemMontagemDTO atualizar(Long id, AtualizarOrdemMontagemDTO dto) {
        OrdemMontagem ordem = buscarOuFalhar(id);

        if (dto.status() != null) {
            ordem.setStatus(dto.status());
        }
        if (dto.prazoCombinado() != null) {
            ordem.setPrazoCombinado(dto.prazoCombinado());
        }
        if (dto.observacoes() != null) {
            ordem.setObservacoes(dto.observacoes());
        }

        OrdemMontagem salva = ordemMontagemRepository.save(ordem);

        if (dto.status() != null) {
            erpNotificacaoService.notificarMudancaStatus(salva);
        }

        return toDTO(salva);
    }

    // ---- Checklist ----

    @Transactional
    public void registrarChecklist(Long ordemId, ChecklistDTO dto) {
        OrdemMontagem ordem = buscarOuFalhar(ordemId);

        ChecklistMontagem checklist = checklistMontagemRepository.findByOrdemMontagemId(ordemId)
                .orElse(ChecklistMontagem.builder().ordemMontagem(ordem).build());

        checklist.setPecasConferidas(dto.pecasConferidas());
        checklist.setMontagemFinalizada(dto.montagemFinalizada());
        checklist.setClienteConfirmou(dto.clienteConfirmou());
        checklist.setFotosUrls(dto.fotosUrls());
        checklist.setObservacoes(dto.observacoes());

        checklistMontagemRepository.save(checklist);

        if (dto.montagemFinalizada() && dto.clienteConfirmou()) {
            ordem.setStatus(StatusOrdemMontagem.CONCLUIDA);
            OrdemMontagem salva = ordemMontagemRepository.save(ordem);
            erpNotificacaoService.notificarMudancaStatus(salva);
        }
    }

    // ---- Ocorrencias ----

    @Transactional
    public OcorrenciaDTO registrarOcorrencia(Long ordemId, OcorrenciaDTO dto) {
        OrdemMontagem ordem = buscarOuFalhar(ordemId);

        Ocorrencia ocorrencia = Ocorrencia.builder()
                .ordemMontagem(ordem)
                .tipo(dto.tipo())
                .descricao(dto.descricao())
                .build();

        Ocorrencia salva = ocorrenciaRepository.save(ocorrencia);

        ordem.setStatus(StatusOrdemMontagem.COM_PENDENCIA);
        OrdemMontagem ordemSalva = ordemMontagemRepository.save(ordem);
        erpNotificacaoService.notificarMudancaStatus(ordemSalva);

        return new OcorrenciaDTO(salva.getId(), salva.getTipo(), salva.getDescricao());
    }

    // ---- Helpers ----

    private OrdemMontagem buscarOuFalhar(Long id) {
        return ordemMontagemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de montagem nao encontrada: " + id));
    }

    private OrdemMontagemDTO toDTO(OrdemMontagem o) {
        List<ItemMontagemDTO> itens = o.getItens().stream()
                .map(i -> new ItemMontagemDTO(i.getId(), i.getProdutoIdExterno(), i.getDescricao(), i.getQuantidade()))
                .toList();

        return new OrdemMontagemDTO(
                o.getId(), o.getOrigem(), o.getStatus(),
                o.getPedidoVendaIdExterno(), o.getChaveNotaFiscal(),
                o.getEmpresaIdExterno(), o.getEmpresaNome(), o.getFilialIdExterno(), o.getFilialNome(),
                o.getClienteIdExterno(), o.getClienteNome(), o.getClienteTelefone(), o.getEnderecoEntrega(),
                o.getPrazoCombinado(), o.getObservacoes(), o.getValorServicoAvulso(),
                itens, o.getCriadaEm(), o.getAtualizadaEm()
        );
    }
}
