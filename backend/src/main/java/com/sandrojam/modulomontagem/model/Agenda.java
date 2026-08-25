package com.sandrojam.modulomontagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "agenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordem_montagem_id", nullable = false)
    private OrdemMontagem ordemMontagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "montador_id", nullable = false)
    private Montador montador;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgenda status;

    @Column(length = 500)
    private String motivoReagendamento;
}
