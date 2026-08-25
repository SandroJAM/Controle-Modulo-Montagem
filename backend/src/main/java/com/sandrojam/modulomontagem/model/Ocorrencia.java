package com.sandrojam.modulomontagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "ocorrencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_montagem_id", nullable = false)
    private OrdemMontagem ordemMontagem;

    @Column(nullable = false)
    private String tipo; // ex: PECA_FALTANTE, AVARIA, REMARCACAO, OUTRO

    @Column(nullable = false, length = 1000)
    private String descricao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime registradaEm;

    @PrePersist
    void prePersist() {
        this.registradaEm = LocalDateTime.now();
    }
}
