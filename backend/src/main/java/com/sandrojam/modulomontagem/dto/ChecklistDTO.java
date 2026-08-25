package com.sandrojam.modulomontagem.dto;

public record ChecklistDTO(
        boolean pecasConferidas,
        boolean montagemFinalizada,
        boolean clienteConfirmou,
        String fotosUrls,
        String observacoes
) {
}
