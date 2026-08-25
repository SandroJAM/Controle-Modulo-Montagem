package com.sandrojam.modulomontagem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "montadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Montador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String telefone;

    /** Bairro/regiao/cidade de atuacao, usado para sugestao de atribuicao. */
    private String areaAtuacao;

    /** Proprio (funcionario) ou terceirizado/autonomo. */
    private boolean terceirizado;

    @Column(nullable = false)
    private boolean ativo = true;
}
