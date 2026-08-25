package com.sandrojam.modulomontagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "checklists_montagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistMontagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_montagem_id", nullable = false, unique = true)
    private OrdemMontagem ordemMontagem;

    private boolean pecasConferidas;

    private boolean montagemFinalizada;

    private boolean clienteConfirmou;

    /** URLs/caminhos das fotos registradas na execucao. Simplificado como
     *  texto separado por virgula neste scaffold inicial. */
    @Column(length = 2000)
    private String fotosUrls;

    @Column(length = 1000)
    private String observacoes;

    private LocalDateTime preenchidoEm;

    @PrePersist
    @PreUpdate
    void marcarPreenchimento() {
        this.preenchidoEm = LocalDateTime.now();
    }
}
