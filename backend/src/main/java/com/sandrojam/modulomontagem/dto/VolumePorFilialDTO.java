package com.sandrojam.modulomontagem.dto;

public record VolumePorFilialDTO(
        String empresaIdExterno,
        String empresaNome,
        String filialIdExterno,
        String filialNome,
        long totalOrdens,
        long concluidas,
        long emAndamento,
        long comPendencia,
        long canceladas
) {
}
