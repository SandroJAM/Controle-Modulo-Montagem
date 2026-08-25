package com.sandrojam.modulomontagem.service;

import com.sandrojam.modulomontagem.dto.VolumePorFilialDTO;
import com.sandrojam.modulomontagem.model.OrigemOrdemMontagem;
import com.sandrojam.modulomontagem.repository.OrdemMontagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final OrdemMontagemRepository ordemMontagemRepository;

    /**
     * Volume de Ordens de Montagem por empresa/filial no periodo informado.
     * Por padrao considera apenas ordens origem = ERP (vinculadas a uma
     * venda real com nota fiscal); passar origem = AVULSA para ver o volume
     * de servicos avulsos separadamente.
     */
    public List<VolumePorFilialDTO> volumePorFilial(LocalDate dataInicio, LocalDate dataFim, OrigemOrdemMontagem origem) {
        LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusDays(30);
        LocalDate fim = dataFim != null ? dataFim : LocalDate.now();
        OrigemOrdemMontagem origemFiltro = origem != null ? origem : OrigemOrdemMontagem.ERP;

        return ordemMontagemRepository.volumePorFilial(
                origemFiltro,
                inicio.atStartOfDay(),
                fim.atTime(23, 59, 59)
        );
    }
}
