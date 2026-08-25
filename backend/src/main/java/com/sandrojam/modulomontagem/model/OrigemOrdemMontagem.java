package com.sandrojam.modulomontagem.model;

/**
 * Indica se a Ordem de Montagem chegou automaticamente via integracao com o
 * ERP (apos emissao de nota fiscal de um pedido com item de montagem) ou se
 * foi criada manualmente dentro do proprio modulo (servico avulso, fora do
 * financeiro por enquanto).
 */
public enum OrigemOrdemMontagem {
    ERP,
    AVULSA
}
