package com.sandrojam.modulomontagem.repository;

import com.sandrojam.modulomontagem.dto.VolumePorFilialDTO;
import com.sandrojam.modulomontagem.model.OrdemMontagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdemMontagemRepository extends JpaRepository<OrdemMontagem, Long> {
    Optional<OrdemMontagem> findByPedidoVendaIdExterno(String pedidoVendaIdExterno);
    boolean existsByChaveNotaFiscal(String chaveNotaFiscal);
    List<OrdemMontagem> findByClienteNomeContainingIgnoreCase(String clienteNome);
    List<OrdemMontagem> findByEmpresaIdExternoAndFilialIdExterno(String empresaIdExterno, String filialIdExterno);

    /**
     * Volume de Ordens de Montagem agrupado por empresa/filial, com quebra
     * por status, no periodo informado. Usado pelo dashboard de volume de
     * montagens por filial.
     */
    @Query("""
            SELECT new com.sandrojam.modulomontagem.dto.VolumePorFilialDTO(
                o.empresaIdExterno,
                o.empresaNome,
                o.filialIdExterno,
                o.filialNome,
                COUNT(o),
                SUM(CASE WHEN o.status = com.sandrojam.modulomontagem.model.StatusOrdemMontagem.CONCLUIDA THEN 1L ELSE 0L END),
                SUM(CASE WHEN o.status = com.sandrojam.modulomontagem.model.StatusOrdemMontagem.EM_ANDAMENTO THEN 1L ELSE 0L END),
                SUM(CASE WHEN o.status = com.sandrojam.modulomontagem.model.StatusOrdemMontagem.COM_PENDENCIA THEN 1L ELSE 0L END),
                SUM(CASE WHEN o.status = com.sandrojam.modulomontagem.model.StatusOrdemMontagem.CANCELADA THEN 1L ELSE 0L END)
            )
            FROM OrdemMontagem o
            WHERE o.origem = :origem
              AND o.criadaEm BETWEEN :inicio AND :fim
            GROUP BY o.empresaIdExterno, o.empresaNome, o.filialIdExterno, o.filialNome
            ORDER BY COUNT(o) DESC
            """)
    List<VolumePorFilialDTO> volumePorFilial(
            @Param("origem") com.sandrojam.modulomontagem.model.OrigemOrdemMontagem origem,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
