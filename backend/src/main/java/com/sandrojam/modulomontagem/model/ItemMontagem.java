package com.sandrojam.modulomontagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "itens_montagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMontagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_montagem_id", nullable = false)
    private OrdemMontagem ordemMontagem;

    /** Id do produto no ERP. Nulo quando a ordem e avulsa e o item nao
     *  corresponde a um produto cadastrado la. */
    private String produtoIdExterno;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidade;
}
