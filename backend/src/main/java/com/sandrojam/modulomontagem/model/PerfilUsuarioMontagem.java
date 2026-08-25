package com.sandrojam.modulomontagem.model;

/**
 * Perfis de acesso do modulo de montagem.
 *
 * ADMIN      - gerencia tudo, inclusive usuarios do modulo.
 * GESTOR     - cria/edita Ordens de Montagem (inclusive avulsas), atribui
 *              montadores e visualiza relatorios.
 * ATENDENTE  - cria Ordens de Montagem avulsas e consulta status.
 * MONTADOR   - visualiza e atualiza apenas as ordens atribuidas a ele
 *              (checklist, ocorrencias).
 */
public enum PerfilUsuarioMontagem {
    ADMIN,
    GESTOR,
    ATENDENTE,
    MONTADOR
}
