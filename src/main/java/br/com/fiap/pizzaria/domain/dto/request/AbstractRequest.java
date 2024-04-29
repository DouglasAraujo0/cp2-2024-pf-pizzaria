package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AbstractRequest(

        @Positive(message = "ID deve ser um número positivo")
        @NotNull(message = "Id deve ser informado")
        Long id
) {
}
