package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SaborRequest(

        @Size(min = 5, max = 50)
        @NotNull(message = "Descrição é obrigatória")
        String descricao,

        @Size(min = 3, max = 50)
        @NotNull(message = "Nome é obrigatório")
        String nome
) {
}
