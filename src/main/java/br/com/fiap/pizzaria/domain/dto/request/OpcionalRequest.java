package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record OpcionalRequest(

        String nome,

        @Valid
        AbstractRequest sabor,

        @Positive(message = "O preco nao pode ser negativo")
        BigDecimal preco
) {
}
