package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.OpcionalRequest;
import br.com.fiap.pizzaria.domain.dto.request.ProdutoRequest;
import br.com.fiap.pizzaria.domain.dto.response.OpcionalResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.dto.response.SaborResponse;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.repository.OpcionalRepository;
import br.com.fiap.pizzaria.domain.repository.SaborRepository;
import br.com.fiap.pizzaria.domain.service.OpcionalService;
import br.com.fiap.pizzaria.domain.service.ProdutoService;
import br.com.fiap.pizzaria.domain.service.SaborService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource implements ResourceDTO<ProdutoRequest, ProdutoResponse> {

    @Autowired
    private ProdutoService service;

    @Autowired
    private OpcionalService opcionalService;

    @Autowired
    private SaborRepository saborRepository;

    @Autowired
    private OpcionalRepository opcionalRepository;

    @GetMapping
    public ResponseEntity<Collection<ProdutoResponse>> findAll(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "preco", required = false) BigDecimal preco,
            @RequestParam(name = "sabor.id", required = false) Long idSabor,
            @RequestParam(name = "opcional.id", required = false) Long idOpcional
    ){

        var opcional = Objects.nonNull( idOpcional ) ? opcionalRepository
                .findById( idOpcional )
                .orElse( null ) : null;

        var sabor = Objects.nonNull( idSabor ) ? saborRepository
                .findById( idSabor )
                .orElse( null ) : null;

        Produto produto = Produto.builder()
                .nome(nome)
                .preco(preco)
                .sabor(sabor)
                .opcionais(opcional != null ? Set.of(opcional) : null)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Produto> example = Example.of( produto, matcher );

        var encontrados = service.findAll( example );

        if (encontrados.isEmpty()) return ResponseEntity.notFound().build();

        var resposta = encontrados.stream()
                .map( service::toResponse )
                .toList();
        return ResponseEntity.ok( resposta );
    }

    @Override
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable Long id) {
        var encontrado = service.findById( id );

        if (encontrado == null) return ResponseEntity.notFound().build();

        var resposta = service.toResponse( encontrado );
        return ResponseEntity.ok( resposta );
    }

    @Override
    @Transactional
    @PostMapping
    public ResponseEntity<ProdutoResponse> save(@RequestBody @Valid ProdutoRequest r) {
        var entity = service.toEntity( r );
        var saved = service.save( entity );
        var resposta = service.toResponse( saved );

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path( "/{id}" )
                .buildAndExpand( saved.getId() )
                .toUri();
        return ResponseEntity.created( uri ).body( resposta );
    }

    @Transactional
    @PostMapping("/{id}/opcionais")
    public ResponseEntity<ProdutoResponse> addOptionalToProduct(@PathVariable Long id, @RequestBody @Valid OpcionalRequest opcionalRequest) {
        var produto = service.findById(id);
        if (Objects.isNull(produto)) return ResponseEntity.notFound().build();

        var opcional = opcionalService.toEntity(opcionalRequest);
        var savedOpcional = opcionalService.save(opcional);

        produto.getOpcionais().add(savedOpcional);
        var updatedProduto = service.save(produto);

        var response = service.toResponse(updatedProduto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/opcionais")
    public ResponseEntity<List<OpcionalResponse>> getOpcionais(@PathVariable Long id) {
        var produto = service.findById(id);
        if (Objects.isNull(produto)) return ResponseEntity.notFound().build();

        var opcionais = produto.getOpcionais();
        var response = opcionais.stream().map(opcionalService::toResponse).toList();
        return ResponseEntity.ok(response);
    }
}
