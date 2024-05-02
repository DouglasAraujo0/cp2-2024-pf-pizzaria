package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.PizzariaRequest;
import br.com.fiap.pizzaria.domain.dto.request.ProdutoRequest;
import br.com.fiap.pizzaria.domain.dto.response.PizzariaResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.dto.response.SaborResponse;
import br.com.fiap.pizzaria.domain.entity.Pizzaria;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.service.PizzariaService;
import br.com.fiap.pizzaria.domain.service.ProdutoService;
import br.com.fiap.pizzaria.domain.service.SaborService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "/pizzarias")
public class PizzariaResource implements ResourceDTO<PizzariaRequest, PizzariaResponse> {

    @Autowired
    private PizzariaService service;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<Collection<PizzariaResponse>> findAll(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "produtoId", required = false) Long produtoId,
            @RequestParam(name = "produto", required = false) String produtoNome
    ) {
        Produto produto = Produto.builder()
                .id(produtoId)
                .nome(produtoNome)
                .build();

        Set<Produto> produtos = new LinkedHashSet<>();
        produtos.add(produto);

        Pizzaria pizzaria = Pizzaria.builder()
                .nome(nome)
                .cardapio(produtos)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues().withIgnoreCase();

        Example<Pizzaria> example = Example.of(pizzaria, matcher);

        var encontrados = service.findAll(example);
        var resposta = encontrados.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(resposta);
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<PizzariaResponse> findById(@PathVariable Long id) {
        var encontrado = service.findById( id );

        if (encontrado == null) return ResponseEntity.notFound().build();

        var resposta = service.toResponse( encontrado );
        return ResponseEntity.ok( resposta );
    }

    @Transactional
    @PostMapping
    @Override
    public ResponseEntity<PizzariaResponse> save(@RequestBody PizzariaRequest r) {
        var entity = service.toEntity(r);
        var saved = service.save(entity);
        var resposta = service.toResponse(saved);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(resposta);

    }

    @Transactional
    @PostMapping(value = "/{id}/cardapio")
    public ResponseEntity<ProdutoResponse> saveOpcional(@PathVariable Long id, @RequestBody ProdutoRequest r){
        Pizzaria pizzaria =  service.findById(id);

        Set<Produto> cardapio = new LinkedHashSet<>();
        if(Objects.nonNull(pizzaria.getCardapio())){
            cardapio = pizzaria.getCardapio();
        }

        if(Objects.isNull(r)){
            return ResponseEntity.badRequest().build();
        }

        var entity = produtoService.toEntity(r);
        cardapio.add(entity);

        var saved = produtoService.save(entity);
        var response = produtoService.toResponse(saved);



        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}/cardapio")
    public ResponseEntity<Collection<ProdutoResponse>> findOpcional(@PathVariable Long id){
        var encontrados = service.findById(id);

        Set<Produto> cardapio = encontrados.getCardapio();

        var response = cardapio.stream().map(produtoService::toResponse).toList();

        return ResponseEntity.ok(response);
    }
}
