package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProdutoRequest(

        @Size(min = 3, max = 50)
        @NotNull(message = "Nome é obrigatório")
        String nome,

        @Valid
        @NotNull(message = "Sabor é obrigatório")
        AbstractRequest sabor,

        BigDecimal preco
) {
}
