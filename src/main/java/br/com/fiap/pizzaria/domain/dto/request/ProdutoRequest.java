package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(

        @NotNull(message = "Nome é obrigatório")
        String nome,

        @Valid
        @NotNull(message = "Sabor é obrigatório")
        AbstractRequest sabor,

        @Positive(message = "O valor não pode ser negativo")
        @NotNull(message = "O preço não pode ser nulo")
        BigDecimal preco
) {
}
