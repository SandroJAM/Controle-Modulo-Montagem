package com.sandrojam.modulomontagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade central do modulo. Representa um servico de montagem, seja ele
 * originado automaticamente de uma venda com nota fiscal emitida no ERP
 * (origem = ERP), seja criado manualmente dentro do proprio modulo
 * (origem = AVULSA).
 *
 * O modulo NAO duplica o cadastro completo de cliente, pedido de venda ou
 * nota fiscal -- esses dados continuam vivendo no ERP. Aqui guardamos apenas
 * as referencias externas e os dados minimos necessarios para operar a
 * montagem (endereco, contato).
 */
@Entity
@Table(name = "ordens_montagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdemMontagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigemOrdemMontagem origem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOrdemMontagem status;

    // ---- Referencias externas (somente quando origem = ERP) ----

    /** Id do pedido de venda no ERP. Nulo quando a ordem e avulsa. */
    private String pedidoVendaIdExterno;

    /** Chave/numero da nota fiscal que disparou a criacao da ordem. */
    private String chaveNotaFiscal;

    // ---- Empresa/filial que emitiu a nota fiscal (origem = ERP) ----
    // Guardadas tambem como texto (nome) para exibicao/relatorio sem
    // depender de nova chamada ao ERP.

    /** Id da empresa no ERP. Obrigatorio quando origem = ERP. */
    private String empresaIdExterno;

    private String empresaNome;

    /** Id da filial no ERP. Obrigatorio quando origem = ERP. */
    private String filialIdExterno;

    private String filialNome;

    // ---- Dados minimos do cliente, copiados no momento da criacao ----

    /** Id do cliente no ERP. Nulo quando a ordem e avulsa e o cliente nao
     *  esta cadastrado la. */
    private String clienteIdExterno;

    @Column(nullable = false)
    private String clienteNome;

    private String clienteTelefone;

    @Column(nullable = false)
    private String enderecoEntrega;

    // ---- Dados operacionais ----

    private LocalDate prazoCombinado;

    @Column(length = 1000)
    private String observacoes;

    /** Preenchido somente para ordens avulsas, que hoje rodam fora do
     *  financeiro do ERP (sem geracao de cobranca/NF por este modulo). */
    private BigDecimal valorServicoAvulso;

    @OneToMany(mappedBy = "ordemMontagem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ItemMontagem> itens = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadaEm;

    private LocalDateTime atualizadaEm;

    @PrePersist
    void prePersist() {
        this.criadaEm = LocalDateTime.now();
        this.atualizadaEm = this.criadaEm;
        if (this.status == null) {
            this.status = StatusOrdemMontagem.CRIADA;
        }
    }

    @PreUpdate
    void preUpdate() {
        this.atualizadaEm = LocalDateTime.now();
    }
}
