package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collection;

public record PizzariaRequest(

        @NotNull(message = "Nome é obrigatório")
        String nome,

        @NotNull(message = "O cardápio não pode ser nulo")
        Collection<AbstractRequest> cardapio
) {
}
