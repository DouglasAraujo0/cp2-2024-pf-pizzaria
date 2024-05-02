package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SaborRequest(

        @NotNull(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Nome é obrigatório")
        String nome
) {
}
